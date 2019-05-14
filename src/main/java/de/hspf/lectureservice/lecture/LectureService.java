package de.hspf.lectureservice.lecture;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@EnableScheduling
public class LectureService {
    Logger logger = LoggerFactory.getLogger(LectureService.class);
    @Autowired
    LectureRepository lectureRepository;

    @Scheduled(fixedDelay = 3600000) // 1h
    private void crawlAllLectureLinks() throws IOException {
        int start = 0;
        ArrayList<String> lectureLinks = new ArrayList<>();
        deleteAllRows();
        int numberOfPages = Integer.parseInt(Jsoup.connect("https://hic.hs-pforzheim.de/lsf/rds?state=wsearchv&search=1&P.vx=lang&P.start=0&P.anzahl=50&veranstaltung.semester=20191").get().select("a.linkAsButton").last().text());
        for (int i = 0; i < numberOfPages; i++){
            String url = "https://hic.hs-pforzheim.de/lsf/rds?state=wsearchv&search=1&P.vx=lang&P.start="+ start +"&P.anzahl=50&veranstaltung.semester=20191";
            try {
                Document doc = Jsoup.connect(url).get();
                Elements elements = doc.select("[width=100%]");
                logger.info("Crawl Page " + (i+1) + " of " + (numberOfPages));
                for(Element element: elements){
                    try{
                        lectureLinks.add(element.select("tbody").get(0).select("tr").get(0).select("td").get(0).select("a").get(0).attr("href"));
                    } catch (Exception e){
                    }
                }
            } catch (IOException e) {
            }
            start += 50;
        }
        getLectureDetails(lectureLinks);
    }

    private void getLectureDetails(ArrayList<String> links) throws IOException {
        logger.info("Start to load lecture details from " + links.size() + " lectures");
        int lectureNumber = 1;
        for(int i = 0; i < links.size(); i++){
            logger.info("Load lecture " + lectureNumber + " of " + links.size());
            Document doc = Jsoup.connect(links.get(i)).get();
            Lecture lecture = new Lecture();
            try{
                lecture.setTitle(doc.select("h3").get(0).text());
                lecture.setCredits(Integer.parseInt(doc.select(":containsOwn(Credits) ~ td").get(0).text()));
                lecture.setSws(Integer.parseInt(doc.select(":containsOwn(SWS) ~ td").get(0).text()));
                lecture.setModulNumber(doc.select(":containsOwn(Kurztext / Modul-Nr.) ~ td").get(0).text());
            }catch (Exception e){}
            if(!lectureRepository.existsByModulNumber(lecture.getModulNumber()) && lecture.getModulNumber() != null){
                lectureRepository.save(lecture);
            }
            lectureNumber++;
        }
    }

    public List<Lecture> loadAllLectures(){
        return lectureRepository.findAll();
    }

    public void deleteAllRows(){
        lectureRepository.deleteAllRows();
    }
}