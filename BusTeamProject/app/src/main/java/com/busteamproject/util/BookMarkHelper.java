package com.busteamproject.util;

import android.content.Context;
import com.busteamproject.AppConst;
import com.busteamproject.DTO.BookMarkDTO;

import java.util.*;

public class BookMarkHelper {
	private static BookMarkHelper instance = null;
	private SharedPreferenceHelper sharedPreferenceHelper;

	private List<BookMarkDTO> bookMarkList = new ArrayList<>();

	private BookMarkHelper(Context context) {
		sharedPreferenceHelper = SharedPreferenceHelper.getInstance(context);
	}

	public static BookMarkHelper getInstance(Context context) {
		if(instance == null) {
			instance = new BookMarkHelper(context);
		}
		return instance;
	}

	public List<BookMarkDTO> getBookMarkList() {
		if (bookMarkList.isEmpty()) {
			Set<String> bookMarkSetList = sharedPreferenceHelper.getStringSet(AppConst.BOOK_MARK_LIST);
			for (String item : bookMarkSetList) {
				String[] splitItem = item.split("\\|");
				try {
					bookMarkList.add(
							new BookMarkDTO(Integer.parseInt(splitItem[0]),
									splitItem[1],
									splitItem[2],
									splitItem[3],
									splitItem[4],
									splitItem[5]));
				} catch (Exception ex) {
				}
			}
			Collections.sort(bookMarkList);
		}
		return bookMarkList;
	}

	// BUS : B , STATION : S
	public void addBookMarkList(String type, String routeId, String stationId, String stationName, String stationNo) {
		if(bookMarkList.isEmpty()) {
			getBookMarkList();
		}

		bookMarkList.add(new BookMarkDTO(bookMarkList.size(), routeId, stationId, type, stationName, stationNo));
		saveBookMarkList();
	}

	public void removeBookMarkList(String type, String value) {
		if(bookMarkList.isEmpty()) {
			getBookMarkList();
		}

		if(type.equals("B")) {
			for(int i = 0; i < bookMarkList.size(); i++) {
				if(bookMarkList.get(i).getRouteId().equals(value)) {
					bookMarkList.remove(i);
					break;
				}
			}
		} else if(type.equals("S")) {
			for(int i = 0; i < bookMarkList.size(); i++) {
				if(bookMarkList.get(i).getStationId().equals(value)) {
					bookMarkList.remove(i);
					break;
				}
			}
		}

		saveBookMarkList();
	}

	public boolean findBookMark(String type, String value) {
		if(bookMarkList.isEmpty()) {
			getBookMarkList();
		}

		if(type.equals("B")) {
			for(int i = 0; i < bookMarkList.size(); i++) {
				if(bookMarkList.get(i).getRouteId().equals(value)) {
					return true;
				}
			}
		} else if(type.equals("S")) {
			for(int i = 0; i < bookMarkList.size(); i++) {
				if(bookMarkList.get(i).getStationId().equals(value)) {
					bookMarkList.remove(i);
					return true;
				}
			}
		}

		return false;
	}

	private void saveBookMarkList() {
		Set<String> saveList = new HashSet<>();
		for(BookMarkDTO item : bookMarkList) {
			saveList.add(item.getSaveData());
		}

		sharedPreferenceHelper.putStringSet(AppConst.BOOK_MARK_LIST, saveList);
	}
}
