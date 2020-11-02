package com.selfhelpindia.kidslearning;

public class SlideList {
    private String uri;
    private String music;
    private String fullText;
    private String oneWord1;
    private String oneWord2;

    public SlideList(String uri, String music, String fullText,String oneWord1,String oneWord2) {
        this.uri = uri;
        this.music = music;
        this.fullText = fullText;
        this.oneWord1 = oneWord1;
        this.oneWord2 = oneWord2;
    }

    public String getUri() {
        return uri;
    }

    public String getMusic() {
        return music;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setMusic(String music) {
        this.music = music;
    }

    public void setFullText(String fullText) {
        this.fullText = fullText;
    }

    public String getFullText() {
        return fullText;
    }

    public String getOneWord1() {
        return oneWord1;
    }

    public void setOneWord1(String oneWord1) {
        this.oneWord1 = oneWord1;
    }

    public String getOneWord2() {
        return oneWord2;
    }

    public void setOneWord2(String oneWord2) {
        this.oneWord2 = oneWord2;
    }
}
