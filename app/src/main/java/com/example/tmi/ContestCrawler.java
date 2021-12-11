package com.example.tmi;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class ContestCrawler {

	private String Title;
	private String First_category;
	private String Second_category;
	private String DDay;
	private String StartDate;
	private String DueDate;
	private String Team;
	private String NumPerson;
	private String MaxNum;
	private String Link;
	private String Image_Link;
	private SaveInfo saveInfo;

	private static String url = "https://wind.gachon.ac.kr/ko/program/all/list/all";
	private static String nomalUrl = "https://wind.gachon.ac.kr/";


	public static int getNumberOfPage(Document doc) {

		/* 전체 페이지 구하기 */
		int total = -1;
		String str = null;

		Elements e30 =  doc.select("label");
		str = e30.get(1).text();
		str = str.replaceAll("[^0-9]", "");
		total = Integer.parseInt(str);
		return total;
	}



	public void LoadExhibition() throws IOException {


		Document doc3 = Jsoup.connect(url).get();

		// 전체 페이지 구하기
		int total_page = getNumberOfPage(doc3);
		int num = total_page / 12;
		int check_count = 0;


		// 페이지만큼 돌면서 크롤링
		for(int i = 1; i <= num; i++) {

			Connection conn = Jsoup.connect(url + "/" + i);

			/* 마감이면 전체 for문 종료 */
			if(check_count == -1) {
				break;
			}


			try {

				Document doc = conn.get();
				Elements elements  = doc.select(".columns-4 li");


				for (Element element : elements) {


					/* 링크 */
					String link = nomalUrl + element.getElementsByAttribute("href").attr("href");


					Document doc2 = Jsoup.connect(link).get();


					/* D-DAY */
					int check;
					String startDate, dueDate, date, due;
					String Dday = null;
					String nnn = null;

					Elements e31 =  element.select("label");
					String check_dday = e31.get(0).text();

					/* 마감이면 종료 */
					if(check_dday.contains("마감")) {
						check_count = -1;
						break;

					}


					//////////////////////////
					/* 마감이 아닐 경우 for문 동작 */
					//////////////////////////


					/* 임박인 경우 체크 */
					if(check_dday.equals("임박")) {

						Elements e44 = doc2.select("time");
						nnn = e44.get(2).text().substring(0, 10);
						nnn = nnn.replace(".", "-");
						LocalDate fromDate = LocalDate.now();
						LocalDate toDate = LocalDate.parse(nnn, DateTimeFormatter.ISO_LOCAL_DATE);
						long Dday2 = ChronoUnit.DAYS.between(fromDate, toDate);
						String ddd = Long.toString(Dday2);
						Dday = "D-" + ddd;
					}

					/* 임박이 아닐 경우 */
					else {
						/* Dday에 m이 있는지 확인 */
						if(check_dday.contains("m")) {
							int m =check_dday.indexOf("m");
							Dday = check_dday.substring(0, m);
						}
						else {
							Dday = check_dday;
						}
					}

					/* 카테고리 */
					Elements e2 = doc2.getElementsByAttributeValue("class", "category");
					Element e3 = e2.get(1);
					String category = e3.select("div").text();
					String first_category = category.substring(0, category.indexOf(" "));
					String second_category = category.substring(category.indexOf(" ") + 1);


					/* 기간 */
					Elements e10 = element.select("small");
					Element e11 = e10.get(3);
					date = e11.text();
					check = date.indexOf("~");

					/* 당일 공모전 */
					if(check != 16) {
						check = date.indexOf("~");
						startDate = date.substring(0, check-1);
						dueDate = date.substring(0, 16) + date.substring(check + 2);

					}
					/* 당일 공모전 아닐 경우 */
					else {
						Elements e4 = doc2.select("time");
						startDate = e4.get(0).text();
						dueDate = e4.get(1).text();
					}


					/* 제목 */
					Elements e1  = element.getElementsByAttributeValue("class", "title");
					String title = e1.get(0).text();

					/* image url */
					Elements e5  = element.getElementsByAttributeValue("class", "cover");
					String tmp= e5.get(0).toString();
					String ImageLink = "no image";	// default
					if(tmp.contains("(")) {
						int first = tmp.indexOf("(");
						int last = tmp.indexOf(")");
						ImageLink = nomalUrl + tmp.substring(first+2, last);
					}


					/* 팀, 개인 */
					Elements e6  = element.getElementsByAttributeValue("class", "info_signin");
					String Sentence = e6.get(0).text();
					int check_num = Sentence.indexOf(" ");
					String team = Sentence.substring(0, check_num);


					/* 신청 인원 */
					String numPerson = Sentence.substring(check_num + 1, Sentence.indexOf("/"));
					String maxNum = Sentence.substring(Sentence.indexOf("/") + 1);

					/* 출력 */

					Title = title;
					First_category = first_category;
					Second_category = second_category;
					DDay = Dday;
					StartDate = startDate;
					DueDate = dueDate;
					Team = team;
					NumPerson = numPerson;
					MaxNum = maxNum;
					Link = link;
					Image_Link = ImageLink;

					saveInfo = new SaveInfo(Title, First_category, DDay, Second_category, StartDate, DueDate,
							Team, NumPerson, MaxNum, Link,  Image_Link);
					saveInfo.infoUpload();


//
//					System.out.println(title + " " + first_category + " " + second_category + "  "+ Dday + " " + startDate + " "
//							+ dueDate + " " + team + " " + numPerson + " " + maxNum);
//					System.out.println("공모전 url: " + link);
//					System.out.println("이미지 url: " + ImageLink);
//					System.out.println();

					// 이미지 url 사이즈 400 * 240 (참고바람)
					// System.out.println에 있는 변수명만 보시면 됩니다!!
					// 모르시는 것은 언제든 질문 주세요!

				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		saveInfo.updateExhibitions();
	}
}