package com.idvl.processing;

import controlP5.*;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProcessingTest extends PApplet {
    private ControlP5 cp5;
    private int x = 1;
    private boolean isMoving = false;
    private Button openBtn, closeBtn, oneBtn, zeroBtn, twoBtn, threeBtn, fourBtn, fiveBtn, sixBtn, sevenBtn, eightBtn, nineBtn, enterBtn, cancelBtn;
    private static final int btnWidth = 50;
    private static final int btnHeight = 50;
    private float keypadBtnScaler = 1 / 6f;
    private float functionalBtnScaler = 1 / 4f;
    private Map<String, Boolean> isSelected = new HashMap();
    private int backgroundColor = color(235, 245, 242);
    private String selectingFloor = "";
    private Textlabel destinations, selecting, nextStop;
    private List<String> selectedFloors = new ArrayList<>();
    private int numberOfFloors = 15;

    public void settings() {
        size(800, 900);
    }

    public void setup() {
        noStroke();
        cp5 = new ControlP5(this);
        cp5.setAutoDraw(false);

        isSelected.put("OPEN", false);
        isSelected.put("CLOSE", false);
        isSelected.put("ONE", false);
        isSelected.put("TWO", false);
        isSelected.put("THREE", false);
        isSelected.put("FOUR", false);
        isSelected.put("FIVE", false);
        isSelected.put("SIX", false);
        isSelected.put("SEVEN", false);
        isSelected.put("EIGHT", false);
        isSelected.put("NINE", false);
        isSelected.put("ENTER", false);

        createMainScreen();
        createOpenButton();
        createCloseButton();
        createKeyPad();
        selectedFloors.add("");
    }

    public void createMainScreen() {

        destinations = cp5.addTextlabel("destination")
                .setText("Destination")
                .setVisible(false)
                .setPosition(20, 20)
                .setSize(760, 60)
                .setColorValue(color(86, 180, 140))
                .setFont(createFont("AppleMyungjo", 40, true));

        selecting = cp5.addTextlabel("selecting")
                .setText("Selecting")
                .setPosition(20, 120)
                .setSize(760, 60)
                .setColorValue(color(0, 0, 0))
                .setFont(createFont("AppleMyungjo", 40));

        nextStop = cp5.addTextlabel("label")
                .setText("Next stop : ")
                .setVisible(false)
                .setPosition(20, 120)
                .setSize(760, 60)
                .setColorValue(color(0, 0, 0))
                .setFont(createFont("AppleMyungjo", 40));
    }

    public void createKeyPad() {
        PImage zeroBtnImg = loadImage("../img/zero.png");
        PImage oneBtnImg = loadImage("../img/one.png");
        PImage twoBtnImg = loadImage("../img/two.png");
        PImage threeBtnImg = loadImage("../img/three.png");
        PImage fourBtnImg = loadImage("../img/four.png");
        PImage fiveBtnImg = loadImage("../img/five.png");
        PImage sixBtnImg = loadImage("../img/six.png");
        PImage sevenBtnImg = loadImage("../img/seven.png");
        PImage eightBtnImg = loadImage("../img/eight.png");
        PImage nineBtnImg = loadImage("../img/nine.png");
        PImage enterBtnImg = loadImage("../img/confirm.png");
        PImage cancelBtnImg = loadImage("../img/cancel.png");

        zeroBtnImg.resize(parseInt(zeroBtnImg.width * keypadBtnScaler), parseInt(zeroBtnImg.width * keypadBtnScaler));
        oneBtnImg.resize(parseInt(oneBtnImg.width * keypadBtnScaler), parseInt(oneBtnImg.width * keypadBtnScaler));
        twoBtnImg.resize(parseInt(twoBtnImg.width * keypadBtnScaler), parseInt(twoBtnImg.width * keypadBtnScaler));
        threeBtnImg.resize(parseInt(threeBtnImg.width * keypadBtnScaler), parseInt(threeBtnImg.width * keypadBtnScaler));
        fourBtnImg.resize(parseInt(fourBtnImg.width * keypadBtnScaler), parseInt(fourBtnImg.width * keypadBtnScaler));
        fiveBtnImg.resize(parseInt(fiveBtnImg.width * keypadBtnScaler), parseInt(fiveBtnImg.width * keypadBtnScaler));
        sixBtnImg.resize(parseInt(sixBtnImg.width * keypadBtnScaler), parseInt(sixBtnImg.width * keypadBtnScaler));
        sevenBtnImg.resize(parseInt(sevenBtnImg.width * keypadBtnScaler), parseInt(sevenBtnImg.width * keypadBtnScaler));
        eightBtnImg.resize(parseInt(eightBtnImg.width * keypadBtnScaler), parseInt(eightBtnImg.width * keypadBtnScaler));
        nineBtnImg.resize(parseInt(nineBtnImg.width * keypadBtnScaler), parseInt(nineBtnImg.width * keypadBtnScaler));
        enterBtnImg.resize(parseInt(enterBtnImg.width * keypadBtnScaler), parseInt(enterBtnImg.width * keypadBtnScaler));
        cancelBtnImg.resize(parseInt(cancelBtnImg.width * keypadBtnScaler), parseInt(cancelBtnImg.width * keypadBtnScaler));

        int padding = 20;
        int baseX = 400 + padding;
        int baseY = 200;
        int btnWidth = oneBtnImg.width;
        int btnHeight = oneBtnImg.height;

        oneBtn = addKeypadButton(oneBtnImg, "one", baseX, baseY);
        twoBtn = addKeypadButton(twoBtnImg, "two", baseX + btnWidth + padding, baseY);
        threeBtn = addKeypadButton(threeBtnImg, "three", baseX + btnWidth * 2 + padding * 2, baseY);
        fourBtn = addKeypadButton(fourBtnImg, "four", baseX, baseY + btnHeight + padding);
        fiveBtn = addKeypadButton(fiveBtnImg, "five", baseX + btnWidth + padding, baseY + btnHeight + padding);
        sixBtn = addKeypadButton(sixBtnImg, "six", baseX + btnWidth * 2 + padding * 2, baseY + btnHeight + padding);
        sevenBtn = addKeypadButton(sevenBtnImg, "seven", baseX, baseY + btnHeight * 2 + padding * 2);
        eightBtn = addKeypadButton(eightBtnImg, "eight", baseX + btnWidth + padding, baseY + btnHeight * 2 + padding * 2);
        nineBtn = addKeypadButton(nineBtnImg, "nine", baseX + btnWidth * 2 + padding * 2, baseY + btnHeight * 2 + padding * 2);
        enterBtn = addKeypadButton(enterBtnImg, "enter", baseX, baseY + btnHeight * 3 + padding * 3);
        zeroBtn = addKeypadButton(zeroBtnImg, "zero", baseX + btnWidth + padding, baseY + btnHeight * 3 + padding * 3);
        cancelBtn = addKeypadButton(cancelBtnImg, "cancel", baseX + btnWidth * 2 + padding * 2, baseY + btnHeight * 3 + padding * 3);
    }

    public Button addKeypadButton(PImage buttonImage, String name, float x, float y) {
        return cp5.addButton(name + "ButtonController")
                .setLabelVisible(false)
                .setPosition(x, y)
                .setImage(buttonImage)
                .updateSize()
                .setColorBackground(backgroundColor)
                .setColorForeground(backgroundColor);
    }

    public void createOpenButton() {
        PImage openBtnImg = loadImage("../img/open-doors.png");
        openBtnImg.resize(parseInt(openBtnImg.width * functionalBtnScaler), parseInt(openBtnImg.height * functionalBtnScaler));

        openBtn = cp5.addButton("openButton")
                .setLabelVisible(false)
                .setPosition(100, 670)
                .setImage(openBtnImg)
                .setColorBackground(backgroundColor)
                .setColorForeground(backgroundColor);
    }

    public void createCloseButton() {
        PImage closeBtnImg = loadImage("../img/close-doors.png");
        closeBtnImg.resize(parseInt(closeBtnImg.width * functionalBtnScaler), parseInt(closeBtnImg.height * functionalBtnScaler));

        closeBtn = cp5.addButton("closeButton")
                .setLabelVisible(false)
                .setPosition(800 - 100 - closeBtnImg.width, 670)
                .setImage(closeBtnImg)
                .setColorBackground(backgroundColor)
                .setColorForeground(backgroundColor);
    }

    public void draw() {
        background(235, 245, 242);
        stroke(90, 184, 253);
        strokeWeight(5);
        line(400, 200, 400, 600);
        drawFloorText();
        cp5.draw();
    }

    public List<String> buildFloorList() {
        List<String> floorNameList = new ArrayList<>();
        floorNameList.add("P");
        floorNameList.add("B");
        floorNameList.add("G");

        for (int i = 1; i <= numberOfFloors; i++) floorNameList.add(String.valueOf(i));

        return floorNameList;
    }

    public void drawFloorText() {

        List<String> floorNameList = buildFloorList();

        textSize(50);
        textAlign(CENTER);

        int baseX1 = 0, baseY1 = 0, baseX2 = 60, baseY2 = 60;
        for (int i = 0; i < numberOfFloors + 3; i++) {
            if (i % 9 == 0 && i != 0) {
                baseY1 += 60;
                baseY2 += 60;
            }
            if (selectedFloors.contains(floorNameList.get(i)))
                fill(186, 122, 20);
            else
                fill(86, 180, 140);
            text(floorNameList.get(i), baseX1 + (i * 60) % 540, baseY1, baseX2 + (i * 60) % 540, baseY2);
            ;
        }
    }

    public void controlEvent(CallbackEvent event) {
        if (event.getAction() == ControlP5.ACTION_CLICK) {
            switch (event.getController().getAddress()) {
                case "/oneButtonController":
                    println("Button one pressed");
                    selectingFloor += "1";
                    break;
                case "/twoButtonController":
                    println("Button two pressed");
                    selectingFloor += "2";
                    break;
                case "/threeButtonController":
                    println("Button three pressed");
                    selectingFloor += "3";
                    break;
                case "/fourButtonController":
                    println("Button four pressed");
                    selectingFloor += "4";
                    break;
                case "/fiveButtonController":
                    println("Button five pressed");
                    selectingFloor += "5";
                    break;
                case "/sixButtonController":
                    println("Button six pressed");
                    selectingFloor += "6";
                    break;
                case "/sevenButtonController":
                    println("Button seven pressed");
                    selectingFloor += "7";
                    break;
                case "/eightButtonController":
                    println("Button eight pressed");
                    selectingFloor += "8";
                    break;
                case "/nineButtonController":
                    println("Button nine pressed");
                    selectingFloor += "9";
                    break;
                case "/zeroButtonController":
                    println("Button zero pressed");
                    selectingFloor += "0";
                    break;
                case "/enterButtonController":
                    println("Button enter pressed");
                    if (checkFloors()) {
                        selectedFloors.add(selectingFloor);
                        selectingFloor = "";
                        updateDestinations();
                    }
                    break;

                case "/cancelButtonController":
                    println("Button cancel pressed");
                    selectingFloor = "";
                    break;
                default:
                    break;
            }
            selecting.setText("Selecting : " + selectingFloor);
        }
    }

    public void updateDestinations() {
        String selected = "";
        for (String floor : selectedFloors) {
            selected += floor + " ";
        }
//        Button newFloorBtn = cp5.addButton(selected)
        destinations.setText("Destinations:" + selected);
    }

    public boolean checkFloors() {
        int floorInt = parseInt(selectingFloor);
        if (floorInt > numberOfFloors) return false;
        return true;
    }

    public void oneButtonController(int value) {
        if (isSelected.get("ONE")) {
            isSelected.put("ONE", false);
            println("Unselected one");
        } else {
            isSelected.put("ONE", true);
            println("Selected one");
        }
    }

    public void openButtonController(int value) {
        if (!isMoving) {
            if (isSelected.get("OPEN")) {
                openBtn.setColorBackground(color(122, 223, 334))
                        .setColorForeground(color(122, 223, 334));
                isSelected.put("OPEN", false);
                println("Door opening unselected");
            } else {
                openBtn.setColorBackground(color(255, 255, 0))
                        .setColorForeground(color(255, 255, 0));
                isSelected.put("OPEN", true);
                println("Door opening selected");
            }
        } else {
            println("Can not select, the elevator is moving");
        }
    }

    public void closeButtonController() {
        if (!isMoving) {

            if (isSelected.get("CLOSE")) {
                closeBtn.setColorBackground(color(255, 255, 0));
                isSelected.put("CLOSE", false);
                println("Door closing unselected");
            } else {
                closeBtn.setColorBackground(color(255, 0, 0));
                isSelected.put("CLOSE", true);
                println("Door closing selected");
            }
        } else {
            println("Can not select, the elevator is moving");
        }
    }

    public static void main(String[] args) {
        PApplet.main(new String[]{"com.idvl.processing.ProcessingTest"});
    }
}
