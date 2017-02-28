

import java.io.IOException;
import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author hakyoshyt
 */
public class Company {
    
    static String baseUrl = "http://search.sunbiz.org";
    static String startUrl = baseUrl + "/Inquiry/CorporationSearch/SearchResults?inquiryType=EntityName&searchNameOrder=A&searchTerm=a"; //make the second part of the string relative to next page
    static String nextPage;
    //Document extractUrl = Jsoup.connect(startUrl).get(); 
    
    String link = null;
    String status = null;
    boolean active = false;
    String companyType = null;
    String companyName = null;
    String dateFiled = null;
    String mailAddress = null;
    String agentName = null;
    String agentAddress = null;
    
    static ArrayList<String> nextPageLinks = new ArrayList<>();
    static ArrayList<Company> companies = new ArrayList<>();
    static ArrayList<Company> activeCompanies = new ArrayList<>();
    
    public Company(String myLink, String myStatus){
        link = myLink;
        status = myStatus;
    }
    
    public Company(String myLink, String myStatus, String myCompanyType, 
            String myCompanyName, String myDateFiled, String myMailAddress, 
            String myAgentName, String myAgentAddress){
        link = myLink;
        status = myStatus;
        companyType = myCompanyType;
        companyName = myCompanyName;
        dateFiled = myDateFiled;
        mailAddress = myMailAddress;
        agentName = myAgentName;
        agentAddress = myAgentAddress;
    }
    
    //Methods
    
    public static void getNextPage(String oldPage) throws IOException{
        if (nextPageLinks.size() == 0){
            nextPageLinks.add(startUrl);
        }
        
        else if (oldPage.equals(startUrl)){
            return;
        }
        
        else if (nextPageLinks.size() == 6){ //LIMITER 
            return;
        }
        
        Document extractNextPageUrl = Jsoup.connect(oldPage).get();
        Elements nextPageUrl =  extractNextPageUrl.select("#maincontent > div:nth-child(4) > div.navigationBarPaging > span:nth-child(2) > a");
        String myNextPage = baseUrl + nextPageUrl.attr("href");
        nextPageLinks.add(myNextPage);
        getNextPage(myNextPage);
    }
    
    public static void initializePage()throws IOException{
        for(int x = 0; x < nextPageLinks.size(); x++){
            for(int i = 0; i < 20; i++){
                Document extractUrl = Jsoup.connect(nextPageLinks.get(x)).get();

                Elements link = extractUrl.select("td.large-width a");
                Elements status = extractUrl.select("td.small-width");

                //Status and link
                String statusH = status.get(i).text();
                String linkH = link.get(i).attr("href");
                companies.add(new Company(linkH, statusH));
//                System.out.println(new Company(linkH, statusH));
            }
        }
    }
    
    public static void findActives(){
        for(int i = 0; i < companies.size(); i++){
            if(companies.get(i).getStatus().equals("Active")){
                companies.get(i).setActive(true);
                activeCompanies.add(companies.get(i));
            }
        }
    }
    
    public static void addActivesValues()throws IOException{
        for(int i = 0; i < activeCompanies.size(); i++){
            Document activeCompanyUrl = Jsoup.connect(baseUrl + activeCompanies.get(i).getLink()).get();
            Elements companyType = activeCompanyUrl.select("div.detailSection.corporationName p:nth-child(1)");
            Elements companyName = activeCompanyUrl.select("div.detailSection.corporationName p:nth-child(2)");
            Elements dateFiled = activeCompanyUrl.select("div.detailSection.filingInformation > span:nth-child(2) > div > span:nth-child(14)");
            Elements mailAddress = activeCompanyUrl.select("div.searchResultDetail > div:nth-child(5) > span:nth-child(2) > div ");
            Elements agentName = activeCompanyUrl.select("div.searchResultDetail > div:nth-child(6) > span:nth-child(2) ");
            Elements agentAddress = activeCompanyUrl.select("div.searchResultDetail > div:nth-child(6) > span:nth-child(3) > div ");
            
            activeCompanies.get(i).setCompanyType(companyType.text());
            activeCompanies.get(i).setCompanyName(companyName.text());
            activeCompanies.get(i).setDateFiled(dateFiled.text());
            activeCompanies.get(i).setMailAddress(mailAddress.text());
            activeCompanies.get(i).setAgentName(agentName.text());
            activeCompanies.get(i).setAgentAddress(agentAddress.text());
            
            System.out.println(activeCompanies.get(i));
        }
    }
    
//    public static void nextPages(Elements s) throws IOException {
//        Document extractNextPageUrl = Jsoup.connect(startUrl).get();
//        Elements nextPage =  extractNextPageUrl.select("#maincontent > div:nth-child(4) > div.navigationBarPaging > span:nth-child(2) > a");
//        String nextPageUrl = baseUrl + nextPage.attr("href");
//        nextPages();
//    }
    
    //Mutators
    
    public void setStatus(String s){
        status = s;
    }
    
    public void setLink(String s){
        link = s;
    }
    
    public void setActive(boolean s){
        active = s;
    }
    
    public void setCompanyType(String s){
        companyType = s;
    }
    
    public void setCompanyName(String s){
        companyName = s;
    }
    
    public void setDateFiled(String s){
        dateFiled = s;
    }
    
    public void setMailAddress(String s){
        mailAddress = s;
    }
    
    public void setAgentName(String s){
        agentName = s;
    }
    
    public void setAgentAddress(String s){
        agentAddress = s;
    }
    
    //Accessors
    
    public String getStatus(){
        return status;
    }
    
    public String getLink(){
        return link;
    }
    
    public boolean getActive(){
        return active;
    }
    
    public String getCompanyType(){
        return companyType;
    }
    
    public String getCompanyName(){
        return companyName;
    }
    
    public String getDateFiled(){
        return dateFiled;
    }
    
    public String getMailAddress(){
        return mailAddress;
    }
    
    public String getAgentName(){
        return agentName;
    }
    
    public String getAgentAddress(){
        return agentAddress;
    }
    
    //toString
    
    public String toString(){
        return  "Status: " + status + "\n" 
                + "Link: " + link + "\n"
                +"Company Type: " + companyType + "\n" 
                + "Company Name: " + companyName + "\n" 
                + "Date Filed: " + dateFiled + "\n" 
                + "Mail Address: " + mailAddress + "\n" 
                + "Agent Name: " + agentName + "\n" 
                + "Agent Address: " + agentAddress;
    }
    
    public static void main(String[] args)throws IOException{
            getNextPage(startUrl);
            System.out.println(nextPageLinks);
            for(int i = 0; i < 5; i++){
                initializePage();
            }
            findActives();
            addActivesValues();
    }
    
}
