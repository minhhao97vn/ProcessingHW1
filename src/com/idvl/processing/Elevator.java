package com.idvl.processing;

import controlP5.*;
import controlP5.Button;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;

import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

public class Elevator extends PApplet {
    private ControlP5 cp5;
    private Button openBtn, closeBtn, oneBtn, twoBtn, threeBtn, fourBtn, fiveBtn;
    private static final int btnWidth = 50;
    private static final int btnHeight = 50;
    private float keypadBtnScaler = 1 / 7f;
    private float functionalBtnScaler = 1 / 4f;
    private int backgroundColor = color(235, 245, 242);
    private Textlabel selecting, nextStop, time, date, currentWeight, currentFloorLabel;
    private int numberOfFloors = 5;
    private DateTimeFormatter timeFormatter, dateFormatter;
    static boolean[] selectedFloor = {false, false, false, false, false};
    private int moveCounter = 0;
    private MovingController movingController = new MovingController();
    private Icon leftArrow, rightArrow;
    private PImage[] leftArrows, rightArrows;

    static int currentFloor = 0, nextFloor = 0;

    // 0 : stand by
    // 1 : Up
    // -1 : Down
    // 2 : Opening door
    // 3 : Closing door
    static int movingState = 0;

    // false : door is not opening
    // true: door is opening
    static boolean isDoorOpened = false;

    private PImage[] oneBtnImg;
    private PImage[] twoBtnImg;
    private PImage[] threeBtnImg;
    private PImage[] fourBtnImg;
    private PImage[] fiveBtnImg;
    PImage[] openBtnImg;
    PImage[] closeBtnImg;

    public class MovingController extends Thread {
        @Override
        public void run() {
            while (true) {
                if (Elevator.currentFloor < Elevator.nextFloor) {
                    try {
                        Elevator.movingState = 1;
                        sleep(1500);
                        Elevator.currentFloor += 1;
                        System.out.println("Moving up " + Elevator.currentFloor);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else if (Elevator.currentFloor == Elevator.nextFloor) {
                    Elevator.movingState = 0;
                    Elevator.selectedFloor[currentFloor] = false;
                    updateButtonColor(currentFloor);
                    break;
                } else {
                    try {
                        Elevator.movingState = -1;
                        sleep(1500);
                        Elevator.currentFloor -= 1;
                        System.out.println("Moving down " + Elevator.currentFloor);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }

    public void settings() {
        size(500, 900);
    }

    public void setup() {
        noStroke();
        cp5 = new ControlP5(this);
        cp5.setAutoDraw(false);

        createMainScreen();
        createOpenButton();
        createCloseButton();
        createKeyPad();
    }

    public void createMainScreen() {

        nextStop = cp5.addTextlabel("label")
                .setText("Next stop: ")
                .setPosition(10, 60)
                .setSize(100, 60)
                .setColorValue(color(91, 185, 252))
                .setFont(createFont("AppleMyungjo", 30));

        timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss", Locale.US);
        dateFormatter = DateTimeFormatter.ofPattern("EEE, MMM dd", Locale.US);

        time = cp5.addTextlabel("time")
                .setText(LocalTime.now().format(timeFormatter))
                .setPosition(350, 10)
                .setSize(100, 60)
                .setColorValue(color(91, 185, 252))
                .setFont(createFont("AppleMyungjo", 30));

        date = cp5.addTextlabel("date")
                .setText(LocalDate.now().format(dateFormatter))
                .setPosition(10, 10)
                .setSize(100, 60)
                .setColorValue(color(91, 185, 252))
                .setFont(createFont("AppleMyungjo", 30));

        currentFloorLabel = cp5.addTextlabel("current")
                .setText("1")
                .setPosition(210, 120)
                .setSize(100, 100)
                .setColorValue(color(186, 122, 20))
                .setFont(createFont("AppleMyungjo", 120));

        leftArrows = new PImage[]{loadImage("../img/go-up.png"), loadImage("../img/go-down.png"), loadImage("../img/go-left.png"), loadImage("../img/go-right.png")};
        rightArrows = new PImage[]{loadImage("../img/go-up.png"), loadImage("../img/go-down.png"), loadImage("../img/go-left.png"), loadImage("../img/go-right.png")};

        leftArrows[0].resize(parseInt(leftArrows[0].width * keypadBtnScaler), parseInt(leftArrows[0].width * keypadBtnScaler));
        leftArrows[1].resize(parseInt(leftArrows[1].width * keypadBtnScaler), parseInt(leftArrows[1].width * keypadBtnScaler));
        leftArrows[2].resize(parseInt(leftArrows[2].width * keypadBtnScaler), parseInt(leftArrows[2].width * keypadBtnScaler));
        leftArrows[3].resize(parseInt(leftArrows[3].width * keypadBtnScaler), parseInt(leftArrows[3].width * keypadBtnScaler));

        rightArrows[0].resize(parseInt(rightArrows[0].width * keypadBtnScaler), parseInt(rightArrows[0].width * keypadBtnScaler));
        rightArrows[1].resize(parseInt(rightArrows[1].width * keypadBtnScaler), parseInt(rightArrows[1].width * keypadBtnScaler));
        rightArrows[2].resize(parseInt(rightArrows[2].width * keypadBtnScaler), parseInt(rightArrows[2].width * keypadBtnScaler));
        rightArrows[3].resize(parseInt(rightArrows[3].width * keypadBtnScaler), parseInt(rightArrows[3].width * keypadBtnScaler));


        leftArrow = cp5.addIcon("leftArrow", 1)
                .setPosition(80, 150)
                .setImage(leftArrows[0])
                .updateSize();

        rightArrow = cp5.addIcon("rightArrow", 1)
                .setPosition(340, 150)
                .setImage(rightArrows[0])
                .updateSize();
    }

    public void createKeyPad() {
        oneBtnImg = new PImage[]{loadImage("../img/one.png"), loadImage("../img/one-selected.png")};
        twoBtnImg = new PImage[]{loadImage("../img/two.png"), loadImage("../img/two-selected.png")};
        threeBtnImg = new PImage[]{loadImage("../img/three.png"), loadImage("../img/three-selected.png")};
        fourBtnImg = new PImage[]{loadImage("../img/four.png"), loadImage("../img/four-selected.png")};
        fiveBtnImg = new PImage[]{loadImage("../img/five.png"), loadImage("../img/five-selected.png")};

        oneBtnImg[0].resize(parseInt(oneBtnImg[0].width * keypadBtnScaler), parseInt(oneBtnImg[0].width * keypadBtnScaler));
        oneBtnImg[1].resize(parseInt(oneBtnImg[1].width * keypadBtnScaler), parseInt(oneBtnImg[1].width * keypadBtnScaler));
        twoBtnImg[0].resize(parseInt(twoBtnImg[0].width * keypadBtnScaler), parseInt(twoBtnImg[0].width * keypadBtnScaler));
        twoBtnImg[1].resize(parseInt(twoBtnImg[1].width * keypadBtnScaler), parseInt(twoBtnImg[1].width * keypadBtnScaler));
        threeBtnImg[0].resize(parseInt(threeBtnImg[0].width * keypadBtnScaler), parseInt(threeBtnImg[0].width * keypadBtnScaler));
        threeBtnImg[1].resize(parseInt(threeBtnImg[1].width * keypadBtnScaler), parseInt(threeBtnImg[1].width * keypadBtnScaler));
        fourBtnImg[0].resize(parseInt(fourBtnImg[0].width * keypadBtnScaler), parseInt(fourBtnImg[0].width * keypadBtnScaler));
        fourBtnImg[1].resize(parseInt(fourBtnImg[1].width * keypadBtnScaler), parseInt(fourBtnImg[1].width * keypadBtnScaler));
        fiveBtnImg[0].resize(parseInt(fiveBtnImg[0].width * keypadBtnScaler), parseInt(fiveBtnImg[0].width * keypadBtnScaler));
        fiveBtnImg[1].resize(parseInt(fiveBtnImg[1].width * keypadBtnScaler), parseInt(fiveBtnImg[1].width * keypadBtnScaler));

        int padding = 20;
        int baseX = 50;
        int baseY = 660;
        int btnWidth = oneBtnImg[0].width;
        int btnHeight = oneBtnImg[0].height;

        oneBtn = addKeypadButton(oneBtnImg[0], "one", baseX, baseY);
        twoBtn = addKeypadButton(twoBtnImg[0], "two", baseX, baseY - btnHeight - padding);
        threeBtn = addKeypadButton(threeBtnImg[0], "three", baseX, baseY - btnHeight * 2 - padding * 2);
        fourBtn = addKeypadButton(fourBtnImg[0], "four", baseX, baseY - btnHeight * 3 - padding * 3);
        fiveBtn = addKeypadButton(fiveBtnImg[0], "five", baseX, baseY - btnHeight * 4 - padding * 4);
    }

    public Button addKeypadButton(PImage buttonImage, String name, float x, float y) {
        return cp5.addButton(name + "ButtonController")
                .setPosition(x, y)
                .setImage(buttonImage)
                .updateSize()
                .setColorBackground(backgroundColor)
                .setColorForeground(backgroundColor);
    }

    public void createOpenButton() {
        openBtnImg = new PImage[]{loadImage("../img/open-doors.png"), loadImage("../img/open-doors-selected.png")};
        openBtnImg[0].resize(parseInt(openBtnImg[0].width * functionalBtnScaler), parseInt(openBtnImg[0].height * functionalBtnScaler));
        openBtnImg[1].resize(parseInt(openBtnImg[1].width * functionalBtnScaler), parseInt(openBtnImg[1].height * functionalBtnScaler));

        openBtn = cp5.addButton("openButtonController")
                .setLabelVisible(false)
                .setPosition(50, 770)
                .setImage(openBtnImg[0])
                .updateSize()
                .setColorBackground(backgroundColor)
                .setColorForeground(backgroundColor);
    }

    public void createCloseButton() {
        closeBtnImg = new PImage[]{loadImage("../img/close-doors.png"), loadImage("../img/close-doors-selected.png")};
        closeBtnImg[0].resize(parseInt(closeBtnImg[0].width * functionalBtnScaler), parseInt(closeBtnImg[0].height * functionalBtnScaler));
        closeBtnImg[1].resize(parseInt(closeBtnImg[1].width * functionalBtnScaler), parseInt(closeBtnImg[1].height * functionalBtnScaler));

        closeBtn = cp5.addButton("closeButtonController")
                .setLabelVisible(false)
                .setPosition(500 - 50 - closeBtnImg[0].width, 770)
                .setImage(closeBtnImg[0])
                .updateSize()
                .setColorBackground(backgroundColor)
                .setColorForeground(backgroundColor);
    }

    public void draw() {
        background(235, 245, 242);
        background(0, 0, 0);
        stroke(148, 137, 115);
        strokeWeight(5);
        line(180, 300, 180, 720);
        cp5.draw();
        drawFloorCaption();
        time.setText(LocalTime.now().format(timeFormatter));
        date.setText(LocalDate.now().format(dateFormatter));
        nextStop.setText("Next stop - " + (nextFloor + 1));
        currentFloorLabel.setText(String.valueOf(currentFloor + 1));
        updateArrows();
    }

    public void updateArrows() {
        switch (movingState) {
            case 0:
                leftArrow.setImage(leftArrows[2]);
                rightArrow.setImage(rightArrows[3]);
                break;
            case 1:
                leftArrow.setImage(leftArrows[0]);
                rightArrow.setImage(rightArrows[0]);
                break;
            case -1:
                leftArrow.setImage(leftArrows[1]);
                rightArrow.setImage(rightArrows[1]);
                break;
        }
    }

    public void updateNextFloor() {
        if (movingState == 0) {
            int deltaF = 10;
            int tempF = currentFloor;
            for (int i = 0; i < numberOfFloors; i++) {
                if (selectedFloor[i] && (deltaF > Math.abs(currentFloor - i)) && (currentFloor != i)) {
                    tempF = i;
                    deltaF = Math.abs(currentFloor - i);
                }
            }
            nextFloor = tempF;
            return;
        }

        if (movingState == 1) {
            for (int i = currentFloor; i < numberOfFloors; i++) {
                if (selectedFloor[i] && (i < nextFloor)) {
                    nextFloor = i;
                    return;
                }
            }
        }
        if (movingState == -1) {
            for (int i = currentFloor; i > 0; i--) {
                if (selectedFloor[i] && (i > nextFloor)) {
                    nextFloor = i;
                    return;
                }
            }
        }
    }

    public void updateButtonColor(int current) {
        switch (current) {
            case 0:
                oneBtn.setImage(oneBtnImg[0]);
                break;
            case 1:
                twoBtn.setImage(twoBtnImg[0]);
                break;
            case 2:
                threeBtn.setImage(threeBtnImg[0]);
                break;
            case 3:
                fourBtn.setImage(fourBtnImg[0]);
                break;
            case 4:
                fiveBtn.setImage(fiveBtnImg[0]);
                break;
            default:
                break;
        }
    }

    public List<String> buildFloorCaptionList() {
        List<String> floorCaptionList = new ArrayList<>();
        floorCaptionList.add("Basement");
        floorCaptionList.add("Gym Center");
        floorCaptionList.add("Shopping Mall");
        floorCaptionList.add("Restaurant");
        floorCaptionList.add("Rooftop Cafe");

        return floorCaptionList;
    }

    public void drawFloorCaption() {
        List<String> floorCaptionList = buildFloorCaptionList();

        textSize(40);
        textAlign(LEFT);

        int padding = 20;
        int baseX1 = 220;
        int baseY1 = 680;
        int baseX2 = 500;
        int baseY2 = baseY1 + oneBtn.getHeight();
        int btnHeight = oneBtn.getHeight();

        for (int i = 0; i < floorCaptionList.size(); i++) {
            text(floorCaptionList.get(i), baseX1, baseY1 - padding * i - btnHeight * i, baseX2, baseY2 - padding * i - btnHeight * i);
        }
    }

    public void controlEvent(CallbackEvent event) throws InterruptedException {
        if (event.getAction() == ControlP5.ACTION_CLICK) {
            switch (event.getController().getAddress()) {
                case "/oneButtonController":
                    println("Button one pressed");
                    if (selectedFloor[0]) {
                        oneBtn.setImage(oneBtnImg[0]);
                        selectedFloor[0] = false;
                    } else {
                        oneBtn.setImage(oneBtnImg[1]);
                        selectedFloor[0] = true;
                    }
                    break;
                case "/twoButtonController":
                    println("Button two pressed");
                    if (selectedFloor[1]) {
                        twoBtn.setImage(twoBtnImg[0]);
                        selectedFloor[1] = false;
                    } else {
                        twoBtn.setImage(twoBtnImg[1]);
                        selectedFloor[1] = true;
                    }
                    break;
                case "/threeButtonController":
                    println("Button three pressed");
                    if (selectedFloor[2]) {
                        threeBtn.setImage(threeBtnImg[0]);
                        selectedFloor[2] = false;
                    } else {
                        threeBtn.setImage(threeBtnImg[1]);
                        selectedFloor[2] = true;
                    }
                    break;
                case "/fourButtonController":
                    println("Button four pressed");
                    if (selectedFloor[3]) {
                        fourBtn.setImage(fourBtnImg[0]);
                        selectedFloor[3] = false;
                    } else {
                        fourBtn.setImage(fourBtnImg[1]);
                        selectedFloor[3] = true;
                    }
                    break;
                case "/fiveButtonController":
                    println("Button five pressed");
                    if (selectedFloor[4]) {
                        fiveBtn.setImage(fiveBtnImg[0]);
                        selectedFloor[4] = false;
                    } else {
                        fiveBtn.setImage(fiveBtnImg[1]);
                        selectedFloor[4] = true;
                    }
                    break;
                case "/openButtonController":
                    println("Button open pressed");

                    break;
                case "/closeButtonController":
                    println("Button close pressed");

                    break;
                default:
                    break;
            }
            updateNextFloor();
            movingController.stop();
            movingController = new MovingController();
            movingController.start();
        }
    }

    public static void main(String[] args) {
        PApplet.main(new String[]{"com.idvl.processing.Elevator"});
    }
}
