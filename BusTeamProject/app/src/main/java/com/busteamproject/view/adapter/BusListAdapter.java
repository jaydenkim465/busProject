package com.busteamproject.view.adapter;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.busteamproject.AppConst;
import com.busteamproject.DTO.BusArrivalInfoDTO;
import com.busteamproject.DTO.WalkingTimeDTO;
import com.busteamproject.R;
import com.busteamproject.api.ApiHelper;
import com.busteamproject.notification.NotificationHelper;
import com.busteamproject.util.BookMarkHelper;
import com.busteamproject.util.SharedPreferenceHelper;
import com.busteamproject.util.Util;
import com.busteamproject.view.ProgressDialog;

import org.json.android.JSONObject;

import java.util.List;

public class BusListAdapter extends ArrayAdapter<BusArrivalInfoDTO> {
	private Context mContext;
	private MyHandler myHandler = new MyHandler();
	private double x = 0d;
	private double y = 0d;

	private List<BusArrivalInfoDTO> busList;
    public BusListAdapter(Context context, int resource, List<BusArrivalInfoDTO> busList) {
        super(context, resource, busList);
		this.mContext = context;
		this.busList = busList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        BusArrivalInfoDTO bus = busList.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_view, parent, false);
        }

        TextView tv = convertView.findViewById(R.id.label);
		TextView tv2 = convertView.findViewById(R.id.label2);
	    Button bookMark = convertView.findViewById(R.id.button_book_mark);
	    Button alarm = convertView.findViewById(R.id.button_alarm);
		alarm.setVisibility(View.VISIBLE);
		bookMark.setVisibility(View.VISIBLE);
		bookMark.setOnClickListener(view -> {
			BookMarkHelper helper = BookMarkHelper.getInstance(getContext());
			if(busList.get(position).isBookMark()) {
				view.setBackground(getContext().getResources().getDrawable(R.drawable.star_48px));
				busList.get(position).setBookMark(false);
				helper.removeBookMarkList("B", busList.get(position).getRouteId());
			} else {
				view.setBackground(getContext().getResources().getDrawable(R.drawable.star_fill_48px));
				busList.get(position).setBookMark(true);
				helper.addBookMarkList("B", busList.get(position).getRouteId(),
						busList.get(position).getStationId(), busList.get(position).getStationName(),
						busList.get(position).getStationName(), busList.get(position).getStationX(),
						busList.get(position).getStationY());
			}
		});

		alarm.setOnClickListener(view -> {
			alarmPopup(parent.getContext(), busList.get(position));
		});

		String tv1String = String.format("%s번 버스 (%s행)", bus.getBusRouteInfo().getRouteName(), bus.getBusRouteInfo().getEndStationName());
		String tv2String = String.format("첫번째 버스 : %s 전 정류소(%s분 후 도착)\n두번째 버스 : %s 전 정류소(%s분 후 도착)",
				bus.getLocationNo1(), bus.getPredictTime1(), bus.getLocationNo2(), bus.getPredictTime2());
		tv.setText(tv1String);
        tv2.setText(tv2String);

		if(bus.isBookMark()) {
			bookMark.setBackground(getContext().getResources().getDrawable(R.drawable.star_fill_48px));
		} else {
			bookMark.setBackground(getContext().getResources().getDrawable(R.drawable.star_48px));
		}

        return convertView;
    }

	private void errorPopup(String msg) {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle("안내")
				.setMessage(msg)
				.setCancelable(false)
				.setPositiveButton("확인", (dialog, which) -> {})
				.create();
		builder.show();
	}

	private void stopService() {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle("안내")
				.setMessage("알림이 작동 중입니다. 중지하고 다시 시도 해주세요.\n중지 하시겠습니까?")
				.setCancelable(false)
				.setPositiveButton("확인", (dialog, which) -> {

					NotificationHelper notificationHelper = new NotificationHelper();
					notificationHelper.stop(mContext);
				})
				.setNegativeButton("취소", ((dialog, which) -> {}))
				.create();
		builder.show();
	}

	public class MyHandler extends Handler {
		@Override
		public void handleMessage(@NonNull Message msg) {
			String data = msg.getData().getString("msg");
			int type = msg.getData().getInt("type");
			if(type == 0) {
				errorPopup(data);
			} else {
				stopService();
			}
		}
	}

	private interface TotalCallback {
		void TotalResult(BusArrivalInfoDTO bus, WalkingTimeDTO path);
	}

	private void alarmPopup(Context context, BusArrivalInfoDTO bus) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("안내")
				.setMessage("현재 위치와 등록된 주소 기준의 알림을 선택해주세요.")
				.setCancelable(false)
				.setPositiveButton("현재위치", (dialog, which) -> {
					if(GpsSearch()) {
						ProgressDialog progressDialog = new ProgressDialog(context);
						progressDialog.show();
						searchPath(bus, (busResult, pathResult) -> {
							progressDialog.dismiss();
							if(busResult != null) {
								if(!executeService(busResult, pathResult)) {
									myHandler.sendMessage(getMessage("알람 등록에 부합하는 버스가 없습니다."));
								}
							} else {
								myHandler.sendMessage(getMessage("알람 등록시 기본 정보 조회에 실패했습니다."));
							}
						});
					} else {
						myHandler.sendMessage(getMessage("현재 위치를 받아 올 수 없습니다."));
					}})
				.setNeutralButton("취소",(dialog, which) -> {})
				.setNegativeButton("주소기준", (dialog, which) -> {
					if(AddressSearch()) {
						ProgressDialog progressDialog = new ProgressDialog(context);
						progressDialog.show();
						searchPath(bus, (busResult, pathResult) -> {
							progressDialog.dismiss();
							if(busResult != null) {
								if(!executeService(busResult, pathResult)) {
									myHandler.sendMessage(getMessage("알람 등록에 부합하는 버스가 없습니다."));
								}
							} else {
								myHandler.sendMessage(getMessage("알람 등록시 기본 정보 조회에 실패했습니다."));
							}
						});
					} else {
						myHandler.sendMessage(getMessage("주소가 등록되지 않았습니다."));
					}
				}).create();
		builder.show();
	}

	private boolean executeService(BusArrivalInfoDTO bus, WalkingTimeDTO path) {
		int walkTime = 0;
		try {
			walkTime = Integer.parseInt(path.getTotalTime());
		} catch (Exception ex) {
		}

		int busTime = -1;
		if(!bus.getPredictTime1().isEmpty()) {
			try {
				busTime = Integer.parseInt(bus.getPredictTime1()) * 60;
			} catch (Exception ex) {}
		} else {
			return false;
		}

		SharedPreferenceHelper helper = SharedPreferenceHelper.getInstance(mContext);
		int defaultTime = helper.getInt(AppConst.ALARM_DEFAULT_TIME) * 60;

		if((defaultTime + walkTime) < busTime) {
			String title = String.format("%s번 버스", bus.getBusRouteInfo().getRouteName());
			String message = bus.getStationName();
			NotificationHelper notificationHelper = new NotificationHelper(title, message, busTime, walkTime, defaultTime);
			if(!notificationHelper.isServiceRunning(mContext)) {
				notificationHelper.start(mContext);
				return true;
			} else {
				myHandler.sendMessage(getMessage("", 1));
				return true;
			}
		}

		if(!bus.getPredictTime2().isEmpty()) {
			try {
				busTime = Integer.parseInt(bus.getPredictTime1()) * 60;
			} catch (Exception ex) {}
		} else {
			return false;
		}

		if((defaultTime + walkTime) < busTime) {
			String title = String.format("%s번 버스", bus.getBusRouteInfo().getRouteName());
			String message = bus.getStationName();
			NotificationHelper notificationHelper = new NotificationHelper(title, message, busTime,  walkTime, defaultTime);
			if(!notificationHelper.isServiceRunning(mContext)) {
				notificationHelper.start(mContext);
				return true;
			} else {
				myHandler.sendMessage(getMessage("", 1));
				return true;
			}
		}
		return false;
	}

	private void searchPath(BusArrivalInfoDTO bus, TotalCallback callBack) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("startX", String.valueOf(x));
		jsonObject.put("startY", String.valueOf(y));
		jsonObject.put("endX", bus.getStationX());
		jsonObject.put("endY", bus.getStationY());
		jsonObject.put("startName","출발지");
		jsonObject.put("endName","도착지");

		new Thread(() -> {
			ApiHelper api = ApiHelper.getInstance();
			JSONObject result = api.tmapGet(mContext, "https://apis.openapi.sk.com/tmap/routes/pedestrian?version=1", jsonObject);
			WalkingTimeDTO path = Util.parseWalkingTime(result);

			String sResult = api.govStringGet("https://apis.data.go.kr/6410000/busarrivalservice/getBusArrivalList",
					"?serviceKey=" + Util.getApiKey(mContext, "busArrivalInfoKey") +
							"&stationId=" + bus.getStationId() + "&routeId=" + bus.getRouteId());
			List<BusArrivalInfoDTO> tempList = Util.parseBusStationArrivalInfo(sResult);
			boolean isExist = false;
			for(int i = 0; i < tempList.size(); i++) {
				if(tempList.get(i).getRouteId().equals(bus.getRouteId())) {
					bus.setPredictTime1(tempList.get(i).getPredictTime1());
					bus.setPredictTime2(tempList.get(i).getPredictTime2());
					isExist = true;
					break;
				}
			}

			if(isExist) {
				callBack.TotalResult(bus, path);
			} else {
				callBack.TotalResult(null, null);
			}
		}).start();
	}

	private boolean GpsSearch() {
		LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
		Location lastKnownLocation = null;
		try {
			lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			if(lastKnownLocation == null) {
				lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			}
		} catch (SecurityException e) {//권한오류 GPS
			e.printStackTrace();
		}

		if (lastKnownLocation != null) {
			x = lastKnownLocation.getLongitude();
			y = lastKnownLocation.getLatitude();
			return true;
		}
		return false;
	}

	private Message getMessage(String input) {
		return getMessage(input, 0);
	}

	private Message getMessage(String input, int type) {
		Message msg = new Message();
		Bundle bundle = new Bundle();
		bundle.putString("msg", input);
		bundle.putInt("type", type);
		msg.setData(bundle);

		return msg;
	}

	private boolean AddressSearch() {
		SharedPreferenceHelper sharedPreferenceHelper = SharedPreferenceHelper.getInstance(mContext);
		String xString = sharedPreferenceHelper.getString(AppConst.MY_HOME_GPS_X);
		String yString = sharedPreferenceHelper.getString(AppConst.MY_HOME_GPS_Y);

		if (!xString.isEmpty() && !yString.isEmpty()) {
			try {
				x = Double.parseDouble(xString);
				y = Double.parseDouble(yString);
				return true;
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return false;
	}
}
