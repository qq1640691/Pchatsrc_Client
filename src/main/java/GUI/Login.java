package GUI;

import Client.*;
import Code.AES;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.sound.sampled.LineUnavailableException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import static Client.Chat.myinf;
import static Client.playwav.play;
import static Client.receivepacket.getfrema;
//import static Client.receivepacket.getvoice;
import static Regular.reguler.*;
import static javasound.getvoice.record;
import static thesendinf.sendinf.fileConvertToByteArray;
import static thesendinf.sendinf.sendmget;


public class Login{

    public static String KEY;
    public static String IP;
    public static int PORT;
    public static String ID;
    public static int BYTELENGTH = 744;
//    public static int openorno = 0;
    public static CopyOnWriteArrayList<String> userlist = new CopyOnWriteArrayList<>();//这个表里存储的是用户列表
    public static CopyOnWriteArrayList<String> userdelay = new CopyOnWriteArrayList<>();//这个表里存储的是客户端之间的延迟
    public static ConcurrentHashMap<String, String> fileinf = new ConcurrentHashMap<>();//这个表里存储的是文件名对应文件信息,创建时间,大小
    public static ConcurrentHashMap<String, CopyOnWriteArrayList<String>> filetopart = new ConcurrentHashMap<>();
    public static CopyOnWriteArrayList<String> titlelsit = new CopyOnWriteArrayList<>();
    public static CopyOnWriteArrayList<byte[]> allbyte = new CopyOnWriteArrayList<>();
    public static CopyOnWriteArrayList<byte[]> filebyte = new CopyOnWriteArrayList<>();
    public static CopyOnWriteArrayList<byte[]> delaybyte = new CopyOnWriteArrayList<>();
    public static ConcurrentHashMap<String, Boolean> areashow = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String,Long> userzx = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String,byte[]> filetemp = new ConcurrentHashMap<>();
    public static String RSApublickey;

    public static Stage all = new Stage();
    public static double WIDTH = 850;
    public static double HEIGH = 430;

    public static void login(DatagramSocket Client) {
        byte[] rsakey = new byte[0];
        try {
            rsakey = fileConvertToByteArray(new File("./RSAKEY"));
        }
        catch (Exception e)
        {
            Stage alert = new Stage();
            Text err = new Text();
            err.setText("无RSA密钥,请检查");
            err.setTextAlignment(TextAlignment.CENTER);
            err.setFill(Color.RED);
            err.setFont(Font.font(null, FontWeight.BOLD, 18));
            HBox theerr = new HBox();
            theerr.setAlignment(Pos.CENTER);
            theerr.getChildren().add(err);
            theerr.setPadding(new Insets(-30, 0, 0, 0));
            Scene scene = new Scene(theerr, 300, 160);
            alert.setScene(scene);
            alert.setResizable(false);
            File ico = new File("ico\\alert.png");
            try {
                alert.getIcons().add(new Image(new FileInputStream(ico)));
            } catch (FileNotFoundException event) {
                throw new RuntimeException(event);
            }
            alert.show();
            alert.setOnCloseRequest(event->{
                System.exit(0);
            });
        }
        RSApublickey = new String(rsakey,0,rsakey.length);
//        System.out.println(RSApublickey);
        Stage stage = new Stage();
        GridPane gridPane = new GridPane();
        HBox welcome = new HBox();
        HBox lg = new HBox();
        welcome.setAlignment(Pos.CENTER);
        lg.setAlignment(Pos.CENTER);
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        gridPane.setPadding(new Insets(-50, 0, 0, 0));
        Text title = new Text("欢迎使用");
        welcome.getChildren().add(title);
        title.setFont(Font.font(null, FontWeight.BOLD, 28));
        title.setFill(Color.rgb(0, 0, 0));
        Label username = new Label("用户名");
        Label aeskey = new Label("AES密钥");
        Label address = new Label("服务器IP");
        Label port = new Label("服务器端口");
        TextField namefile = new TextField();
        namefile.setText("方正"+ new Random().nextInt(100));
        PasswordField keyfile = new PasswordField();
        keyfile.setText("0000000000000000");
        TextField addressfile = new TextField();
        addressfile.setText("47.113.189.105");
        TextField portfile = new TextField();
        portfile.setText("41000");
        title.setTextAlignment(TextAlignment.CENTER);
        gridPane.add(username, 0, 1);
        gridPane.add(namefile, 1, 1);
        gridPane.add(aeskey, 0, 2);
        gridPane.add(keyfile, 1, 2);
        gridPane.add(address, 0, 3);
        gridPane.add(port, 0, 4);
        gridPane.add(addressfile, 1, 3);
        gridPane.add(portfile, 1, 4);
        gridPane.add(welcome, 0, 0, 11, 1);
        Button loginbutton = new Button();
        loginbutton.setText("登录");
        loginbutton.setFont(Font.font(null, FontWeight.BOLD, 15));
        lg.getChildren().add(loginbutton);
        gridPane.add(lg, 0, 5, 11, 1);
        gridPane.setAlignment(Pos.CENTER);
        Text zhexy = new Text();
        zhexy.setText("用户协议");
        zhexy.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            Text xycontent = new Text();
            xycontent.setTextAlignment(TextAlignment.CENTER);
            xycontent.setFont(Font.font(null,FontWeight.BOLD,15));
            String content=
                    "为使用本软件及服务，您应当阅读并遵守《本软件许可协议》（以下简称（本协议）。请您务必审慎阅读，\n" +
                    "从分理解各条款内容，特别是免除或者限制责任的条款，以及开通或使用某项服务的单独协议，并选择接受或不接受。限制，免责条款可能以加粗形式提示您注意。\n" +
                    "除非您已阅读并接受本协议所有条款，否则您无权下载，安装或使用本软件及相关服务。您的下载，安装，登录等使用行为即视为您已阅读并同意上述协议的约束。 \n" +
                    "如果您未满18周岁，请在法定监护人的陪同下阅读本协议及其他上述协议，并特别注意未成年人使用条款。\n" +
                    "一， 协议的范围\n" +
                    "本协议是您与本软件之间关于您下载，安装，使用，复制本软件，以及使用本软件相关服务所订立的协议。\n" +
                    "二， 关于本服务\n" +
                    "本服务内容是指本软件客户端软件提供包括但不限于IOS及Android等多个版本，您必须选择与所安装手机相匹配的软件版本。\n" +
                    "三， 软件的获取\n" +
                    "您可以直接从本软件授权的第三方获取。\n" +
                    "如果您从未经本软件授权的第三方获取本软件或与本软件名称相同的安装程序，本软件无法保证该软件能够正常使用，并对因此给您造成的损失不予负责。下载安装程序后，您需要按照该程序提示的步骤正确安装。\n" +
                    "为提供更加优质，安全的服务，在本软件安装时本软件可能推荐您安装其他软件，您可以选择安装或不安装。\n" +
                    "如果您不再需要使用本软件或者需要安装新版本软件，可以自行卸载。\n" +
                    "四， 软件的更新\n" +
                    "为了改善用户体验，完善服务内容，本软件将不断努力开发新的服务，并为您不时提供软件更新(这些更新可能会采取软件替换，修改，功能强化，版本升级等形式)。\n" +
                    "为了保证本软件及服务的安全性和功能的一致性，本软件有权不向您特别通知而对软件进行更新，或者对软件的部分功能效果进行改变或限制。\n" +
                    "本软件新版本发布后，旧版本的软件可能无法使用，本软件部保证旧版本软件继续可用及相应的服务，请您随时核对并下载最新版本。\n" +
                    "五， 用户个人信息保护\n" +
                    "保护用户个人信息是本软件的一项基本原则，本软件将会采取合理的措施保护用户的信息。除法律法规规定的情形外，未经用户许可本软件不会向第三方公开，透漏用户个人信息。\n" +
                    "为了向用户提供相关服务功能或改善技术和服务，您在注册账号或使用本服务的过程中，可能需要提供一些必要信息，本软件对相关信息采用国际化标准的加密存错与传输方式，保障用户个人信息的安全。\n" +
                    "未经您的同意，本软件不会向本软件以外的任何公司，组织和个人披露您的个人信息，但法律法规另有规定的除外。\n" +
                    "本软件非常重视对未成年人个人信息的保护。若您是18周岁以下的未成年人，在使用本软件的服务前，应事先取得您家长或法定监护人的书面同意。\n" +
                    "六， 主权力义务条款\n" +
                    "本软件特别提醒您应妥善保管您的账号，当您使用完毕后，应安全退出。\n" +
                    "用户注意事项：您的理解并同意，为了向您提供有效的服务，您在此许可本软件利用您移动通讯终端设备的处理器和宽带等资源。本软件使用过程中可能产生的数据流量的费用，您需自行向运营商了解相关资费信息。\n" +
                    "七， 用户行为规范\n" +
                    "您在使用本服务时需遵守法律法规，社会主义制度，国家利益，公民合法权利，社会公共秩序，道德风尚及信息真实性等“七条底线“要求。\n" +
                    "八， 软件使用规范\n" +
                    "除非法律允许或本软件的书面许可，您使用本软件过程中不得删除本软件及其副本上关于知识产权的信息，不得对本软件进行反向工程等或以其他方式尝试发现本软件的源代码。\n" +
                    "九， 对自己行为负责\n" +
                    "您充分了解并同意，您必须为自己注册账号下的一切行为负责。\n" +
                    "十， 其他\n" +
                    "您使用本软件即视为您已阅读并同意接受本软件协议的约束。本软件有权在必要时修改本协议条款。如果您不接受修改后的条款，应当停止使用本软件。"+
                            "\n"+"\n"+"by FANGZHENG FRESHWATER\n"+"方正著作权所有"+"\nQQ 1640691243";
            xycontent.setText(content);
            HBox hBoxxy = new HBox();
            hBoxxy.setAlignment(Pos.CENTER);
            hBoxxy.getChildren().add(xycontent);
            Scene scenexy = new Scene(hBoxxy, 1500, 800);
            Stage stagexy = new Stage();
            stagexy.setScene(scenexy);
            stagexy.alwaysOnTopProperty();
            stagexy.setResizable(false);
            File ico = new File("ico\\alert.png");
            try {
                stagexy.getIcons().add(new Image(new FileInputStream(ico)));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            stagexy.show();
        });
        zhexy.setFont(Font.font(null, FontWeight.BOLD, 13));
        RadioButton yhxy = new RadioButton();
        /*
          默认按钮被选中,测试需要
         */
//        yhxy.setSelected(true);
        loginbutton.addEventHandler(MouseEvent.MOUSE_CLICKED, e ->
        {
            if (Objects.equals(e.getButton().toString(), "PRIMARY")) {
                if (!yhxy.isSelected()) {
                    Stage aleat = new Stage();
                    Text err = new Text();
                    err.setText("请先同意用户协议!");
                    err.setTextAlignment(TextAlignment.CENTER);
                    err.setFill(Color.RED);
                    err.setFont(Font.font(null, FontWeight.BOLD, 25));
                    Popbox(aleat, err);
                } else {
                    String getname = namefile.getText();
                    String getkey = keyfile.getText();
                    String getaddress = addressfile.getText();
                    String getport = portfile.getText();
                    if (isSpecialChar(getname) && isIPAdress(getaddress) && isport(getport) && getname.getBytes(StandardCharsets.UTF_8).length <= 30 && !isfigure(getname)) {
                        chat(Client, getaddress, getport, getname, getkey);
                        stage.close();
                    } else {
                        if (!isSpecialChar(getname)) {
                            Stage aleat = new Stage();
                            Text err = new Text();
                            err.setText("用户名违法,不得包含特殊字符");
                            err.setTextAlignment(TextAlignment.CENTER);
                            err.setFill(Color.RED);
                            err.setFont(Font.font(null, FontWeight.BOLD, 15));
                            Popbox(aleat, err);
                        }
                        if (!isport(getport)) {
                            Stage aleat = new Stage();
                            Text err = new Text();
                            err.setText("端口号违法,请检查是否在1-65536之间");
                            err.setTextAlignment(TextAlignment.CENTER);
                            err.setFill(Color.RED);
                            err.setFont(Font.font(null, FontWeight.BOLD, 15));
                            Popbox(aleat, err);
                        }
                        if (!isIPAdress(getaddress)) {
                            Stage aleat = new Stage();
                            Text err = new Text();
                            err.setText("IP地址违法,请检查");
                            err.setTextAlignment(TextAlignment.CENTER);
                            err.setFill(Color.RED);
                            err.setFont(Font.font(null, FontWeight.BOLD, 18));
                            Popbox(aleat, err);
                        }
                        if(isfigure(getname))
                        {
                            Stage aleat = new Stage();
                            Text err = new Text();
                            err.setText("用户名不能是纯数字");
                            err.setTextAlignment(TextAlignment.CENTER);
                            err.setFill(Color.RED);
                            err.setFont(Font.font(null, FontWeight.BOLD, 18));
                            Popbox(aleat, err);
                        }
                        if(getname.getBytes(StandardCharsets.UTF_8).length > 30 )
                        {
                            Stage aleat = new Stage();
                            Text err = new Text();
                            err.setText("用户名长度超出,请重新输入");
                            err.setTextAlignment(TextAlignment.CENTER);
                            err.setFill(Color.RED);
                            err.setFont(Font.font(null, FontWeight.BOLD, 18));
                            Popbox(aleat, err);
                        }

                    }
                }
            }
        });
        HBox xy = new HBox();
        xy.getChildren().add(yhxy);
        xy.getChildren().add(zhexy);
        xy.setPadding(new Insets(0, 15, -230, 0));
        xy.setAlignment(Pos.CENTER);
        gridPane.add(xy, 0, 6, 11, 1);
        Scene scene = new Scene(gridPane, 700, 400);
        stage.setScene(scene);
        stage.setResizable(false);
        File ico = new File("ico\\login.png");
        try {
            stage.getIcons().add(new Image(new FileInputStream(ico)));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        stage.show();
        stage.setOnCloseRequest(event -> {
            System.exit(0);
        });
    }

    public static void Popbox(Stage aleat, Text err) {
        POP(aleat, err);
    }

    public static void POP(Stage aleat, Text err) {
        HBox theerr = new HBox();
        theerr.setAlignment(Pos.CENTER);
        theerr.getChildren().add(err);
        theerr.setPadding(new Insets(-30, 0, 0, 0));
        Scene scene = new Scene(theerr, 300, 160);
        aleat.setScene(scene);
        aleat.setResizable(false);
        File ico = new File("ico\\alert.png");
        try {
            aleat.getIcons().add(new Image(new FileInputStream(ico)));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        aleat.show();
    }

    public static ObservableList<String> data = FXCollections.observableArrayList();
    public static ListView<String> listView = new ListView<>(data);
    public static TextArea inputarea = new TextArea();
    public static ObservableList<Object> getdata = FXCollections.observableArrayList();
    public static ListView<Object> getlist = new ListView<>(getdata);
    public static TextArea infarea = new TextArea();
    public static ConcurrentHashMap<String, String> thefilepath = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String,byte[]> videostrem = new ConcurrentHashMap<>();
    public static CopyOnWriteArrayList<String> longtime = new CopyOnWriteArrayList<>();

    public static ConcurrentHashMap<String,byte[]> videostremv = new ConcurrentHashMap<>();
    public static CopyOnWriteArrayList<byte[]> packetbytes = new CopyOnWriteArrayList<>();

    public static CopyOnWriteArrayList<String> showedtime = new CopyOnWriteArrayList<>();


    public static void chat(DatagramSocket Client, String ip, String port, String userid, String key) {
        new Thread(()->{
                while(true) {
                    for (byte[] getbytes : packetbytes) {
                        try {
                            byte[] bytes = AES.decrypt(getbytes, KEY);
                            String head = new String(bytes, 0, 2);
                            switch (head) {
                                case "01":
                                    String[] inf = new String(bytes, 0, 50).split("//");
                                    if(!showedtime.contains(inf[2]))
                                    {
                                        getfrema(bytes, inf[2], inf[3], inf[1]);
                                    }
                                    break;
                                case "00":
                                    String[] infv = new String(bytes, 0, 50).split("//");
                                    byte[] temp = new byte[bytes.length-50];
                                    System.arraycopy(bytes,50,temp,0,bytes.length-50);
                                    videostremv.put(infv[1],temp);
                                    break;
                                case "fi":
                                    filebyte.add(bytes);
                                    break;
                                case "de":
                                    delaybyte.add(bytes);
                                default:
                                    allbyte.add(bytes);
                            }
                            packetbytes.remove(getbytes);
                        } catch (Exception e) {
                            System.out.println("error");
                        }
                    }
                }
            }).start();
        Path message = Paths.get("allmessage");
        try {
            Files.createDirectories(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        IP = ip;
        KEY = key;
        PORT = Integer.parseInt(port);
        ID = userid;
        /**
         * 向服务器发送在线的线程,直接在开头就会运行
         */
        Thread useronline = new useronline(userzx,userlist);
        useronline.start();
        Thread chaeckdelay = new checkdelay();
        chaeckdelay.start();
        Thread senddely = new senddelay(Client);
        senddely.start();
        Thread UDPonline = new UDPonline();
        UDPonline.start();
        Thread sendonline = new sendonline(IP, PORT, ID, Client);
        sendonline.start();
        Thread receive = new recieveall(Client, userlist,listView,data,getdata);
        receive.start();
        Thread printfileget = new printfileget(infarea);
        printfileget.start();
        Thread checkfile = new checkfile(Client);
        checkfile.start();
        /**
         * 这个地方从列表点击获取,要从主函数里删掉
         */
        HBox Hmessage = new HBox();//左上角的聊天框
        HBox intitle = new HBox();
        intitle.setMinHeight(30);
        Text listtitle = new Text();
        /**
         * List的接口,传用户列表进去
         */
        listView.setMinWidth(330);
        listView.setMaxWidth(330);
        listView.setMinHeight(260);
        listView.getStylesheets().add("the.css");
        listtitle.setText("在线用户列表");
        listView.getSelectionModel().selectedItemProperty().addListener(
                (ObservableValue<? extends String> ov, String old_val,
                 String new_val) -> {
                    if (new_val != null) {
                        String theid = new_val.split("//")[2];
                        if (!Objects.equals(theid, ID) && !titlelsit.contains(new_val)) {
                            titlelsit.add(new_val);
                            chatone(Client, new_val, null);
                            areashow.put(new_val, true);
                        }
                    }
                });
        listtitle.setFont(Font.font(null, FontWeight.BOLD, 18));
        intitle.getChildren().add(listtitle);
        intitle.setAlignment(Pos.CENTER);
        GridPane top = new GridPane();
        inputarea.setWrapText(true);
        inputarea.getStylesheets().add("the.css");
        getlist.getStylesheets().add("the.css");
        getlist.setMinHeight(260);
        HBox titlebox = new HBox();
        titlebox.setAlignment(Pos.CENTER);
        getlist.setMaxWidth(520);
        getlist.setMinWidth(520);
        Text infareatitle = new Text();
        infareatitle.setText("用户聊天消息");
        infareatitle.setTextAlignment(TextAlignment.CENTER);
        infareatitle.setFill(Color.rgb(70,189,153));
        infareatitle.setFont(Font.font(null, FontWeight.BOLD, 25));
        titlebox.getChildren().add(infareatitle);
        getdata.add(titlebox);
        top.add(getlist,0,0,2,1);
        Button send = new Button();
        /**
         * 发送消息的按钮接口
         */
        send.setText("发送消息");
        send.setMinWidth(55);
        send.setMinHeight(25);
        send.getStylesheets().add("the.css");
        HBox textk = new HBox();
        /**
         * 发送文件的按钮接口
         */
        textk.setAlignment(Pos.CENTER);
        textk.setMinWidth(55);
        textk.setMinHeight(25);
//        textk.setMaxHeight(30);
        Hmessage.getChildren().add(top);
        top.add(textk, 0, 1, 1, 1);
//        top.add(filesend,1,1,1,1);
        HBox Hgetinput = new HBox();//左下的输入框
        GridPane TEMP = new GridPane();
        TEMP.setMaxWidth(520);
        TEMP.setMinWidth(520);
//        TEMP.setGridLinesVisible(true);
        HBox HSEND = new HBox();
        HSEND.setAlignment(Pos.CENTER_RIGHT);
        HSEND.getChildren().add(send);
        TEMP.add(inputarea, 0, 0, 1, 1);
        TEMP.add(HSEND, 0, 1, 1, 1);
        Hgetinput.getChildren().add(TEMP);
        VBox Userlist = new VBox();//右边的用户列表
        Background hm = new Background(new BackgroundFill(Color.rgb(245, 245, 245), CornerRadii.EMPTY, Insets.EMPTY));
        Hmessage.setBackground(hm);
        BorderPane borderPane = new BorderPane();//自由布局
        Scene scene = new Scene(borderPane, WIDTH, HEIGH);
        /**
         *
         */
        send.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            Thread sendmessgaeall = new sendmessageall(Client, userlist, inputarea);
            sendmessgaeall.start();
        });
        Interface(Hmessage,top,textk,Hgetinput,scene,getlist);
        Userlist.setMinWidth(330);
        scene.widthProperty().addListener((observable, oldValue, newValue) -> {
            Hmessage.setMinWidth(520 + (double) newValue - WIDTH);
            Hgetinput.setMinWidth(520 + (double) newValue - WIDTH);
            inputarea.setMinWidth(520 + (double) newValue - WIDTH);
            TEMP.setMaxWidth(520 + (double) newValue - WIDTH);
            getlist.setMaxWidth(520+(double)newValue-WIDTH);
            getlist.setMinWidth(520+(double)newValue-WIDTH);
        });
        scene.heightProperty().addListener((observable, oldValue, newValue)->{
            listView.setMinHeight(260+(double)newValue-HEIGH);
        } );
        GridPane bt = new GridPane();
        bt.add(Hgetinput, 0, 0, 1, 1);
        /**
         * 这里是最右边的文本框,打印后台输出消息
         */
        infarea.setWrapText(true);
        infarea.getStylesheets().add("the.css");
        infarea.setPrefSize(330, 140);
        infarea.setEditable(false);
        bt.add(infarea, 1, 0, 1, 1);
        borderPane.setLeft(Hmessage);
        borderPane.setBottom(bt);
        borderPane.setRight(Userlist);
        Thread receivefile = new receivefile(Client,infarea,thefilepath);
        receivefile.start();
        Userlist.getChildren().add(intitle);
        Userlist.getChildren().add(listView);
        all.setScene(scene);
        all.setTitle( ID+"   群聊");
        Thread linkuser = new Linkuser(infarea);
        linkuser.start();
        all.setOnCloseRequest(event -> {
            System.exit(0);
        });
        File ico = new File("ico\\Message.png");
        try {
            all.getIcons().add(new Image(new FileInputStream(ico)));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        all.show();
    }

    public static void Interface(HBox hmessage, GridPane top, HBox textk, HBox hgetinput, Scene scene,ListView<Object> getlist) {
        top.setMinHeight(290);
        hgetinput.setMaxHeight(140);
        hgetinput.setMinHeight(140);
        textk.setMaxHeight(30);
        scene.heightProperty().addListener((observable, oldValue, newValue) -> {
            hmessage.setMinHeight(290 + (double) newValue - HEIGH);
            getlist.setMinHeight(260+(double) newValue -HEIGH);
            top.setMinHeight(290 + (double) newValue - HEIGH);
            hgetinput.setMaxHeight(140);
            hgetinput.setMinHeight(140);
        });
        scene.widthProperty().addListener((observable, oldValue, newValue) -> {
            hmessage.setMaxWidth(520+(double)newValue);
        hmessage.setMinWidth(520+(double)newValue);
        hgetinput.setMaxWidth(520+(double)newValue);
        hgetinput.setMinWidth(520+(double)newValue);
        });
        all.setMinHeight(480);
        all.setMinWidth(880);
    }

    public static int Close=1;


    public static void chatone(DatagramSocket Client, String title, byte[] result) {
        CopyOnWriteArrayList<String> mess = new CopyOnWriteArrayList<>();
        Path files = Paths.get("file\\"+title.split("//")[2]);
        Path image = Paths.get("image\\"+title.split("//")[2]);
        Path voice = Paths.get("voice\\"+title.split("//")[2]);
        Path message = Paths.get("message\\"+title.split("//")[2]);
        try {
            Files.createDirectories(files);
            Files.createDirectories(image);
            Files.createDirectories(voice);
            Files.createDirectories(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Stage onepeople = new Stage();
        onepeople.setTitle(title);
        /**
         * 向服务器发送在线的线程,直接在开头就会运行
         */
        HBox Hmessage = new HBox();//左上角的聊天框
        GridPane top = new GridPane();
        /**
         * 输入区的接口,获取输入内容
         */
        TextArea inputarea = new TextArea();
        inputarea.setMinWidth(520);
        /**
         * 输出区的接口,打印别人说的内容
         */
        ObservableList<Object> getdata = FXCollections.observableArrayList();
        ListView<Object> getlist = new ListView<>(getdata);
        HBox titlebox = new HBox();
        titlebox.setAlignment(Pos.CENTER);
        getlist.setMinWidth(520);
        Text infareatitle = new Text();
        infareatitle.setText("聊天消息");
        infareatitle.setTextAlignment(TextAlignment.CENTER);
        infareatitle.setFill(Color.rgb(70,189,153));
        infareatitle.setFont(Font.font(null, FontWeight.BOLD, 25));
        titlebox.getChildren().add(infareatitle);
        getdata.add(titlebox);
        getlist.setEditable(false);
        if (result != null) {
            String getd = new String(result, 0, 256);
            String[] infd = getd.split("//");
            SimpleDateFormat sdfd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date timed = new Date(); // 获取当前时间
            String formattime = sdfd.format(timed);// 格式化时间
            getdata.add(infd[3] + ":" + formattime);
            TextArea print = new TextArea();
                    print.setWrapText(true);
                    print.setEditable(false);
                    print.setPrefSize(500,100);
                    print.setStyle("-fx-font-size: 18 ;-fx-font-weight:bold");
            print.setText(new String(result, 256, result.length - 256));
            getdata.add(print);
            getlist.setItems(getdata);
            String inf = new String(result,0,256);
            SocketAddress address = new InetSocketAddress(inf.split("//")[1], Integer.parseInt(inf.split("//")[2]));
            sendmget(address, Client, new String(result, 256, result.length - 256).hashCode(), myinf());
            method3("message\\"+getd.split("//")[3]+"\\"+"allmessage.txt",Login.ID + ":" + formattime+"\n"+new String(result, 256, result.length - 256));
        }
        inputarea.setWrapText(true);
        inputarea.getStylesheets().add("the.css");
        getlist.getStylesheets().add("the.css");
        getlist.setMinHeight(260);
        top.add(getlist, 0, 0, 2, 1);
        Button send = new Button();
        /**
         * 发送消息的按钮接口
         */
        send.setText("发送消息");
        send.addEventHandler(MouseEvent.MOUSE_CLICKED, e ->
        {
            Thread sendoneuser = new sendoneuser(Client, inputarea, title, getdata,getlist,mess);
            sendoneuser.start();
        });
        Thread showimage = new showimage(getdata,getlist,title);
        showimage.start();
        send.setMinWidth(55);
        send.setMinHeight(25);
        send.getStylesheets().add("the.css");
        HBox textk = new HBox();
        /**
         * 发送文件的按钮接口
         */
        Text functiondes = new Text(" 发送文件  ");
        Text f2 = new Text("  发送图片  ");
        Text f3 = new Text("  发送语音  ");
        Text f4 = new Text("  视频聊天  ");
        f4.addEventHandler(MouseEvent.MOUSE_CLICKED,e->{
            Close=1;
            String[] inf = title.split("//");
            Thread vchat = new Vchat(inf[0].replace("/",""),Integer.parseInt(inf[1]),Client);
            vchat.start();
            Thread showthevideo = new showvideo();
            showthevideo.start();
        });
        functiondes.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("选择文件");
            File file = fileChooser.showOpenDialog(new Stage());
            if(file!=null) {
                if((file.getName()).getBytes(StandardCharsets.UTF_8).length>60||file.length()>2146483648L)
                {
                    infarea.appendText("文件名过长或文件大于2G,请检查");
                }
                else{ if (thefilepath.containsKey(file.getName())) {
                    infarea.appendText("文件已经发送,不允许重复发送");
                    } else {
                        String flag = "";
                        Thread sendfile = new sendfile(Client, file, title,thefilepath,flag);
                        sendfile.start();
                    }
                }
            }
        });
        /**
         * 发图片
         */
        f2.addEventHandler(MouseEvent.MOUSE_CLICKED, e->{
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("选择图片");
            fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("GIF", "*.gif"),
                new FileChooser.ExtensionFilter("BMP", "*.bmp"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
            );
            File file = fileChooser.showOpenDialog(new Stage());
            if(file!=null) {
                if((file.getName()).getBytes(StandardCharsets.UTF_8).length>60||file.length()>2146483648L)
                {
                    infarea.appendText("文件名过长或文件大于2G,请检查");
                }
                else{ if (thefilepath.containsKey(file.getName())) {
                    infarea.appendText("文件已经发送,不允许重复发送");
                    } else {
                        String flag = "fzimage";
                        Thread sendfile = new sendfile(Client, file, title,thefilepath,flag);
                         sendfile.start();
                    }
                }
            }
        });
        f3.addEventHandler(MouseEvent.MOUSE_CLICKED,e->{
            try {
                record(Client,title,thefilepath);
            } catch (LineUnavailableException | InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        });
        textk.getChildren().add(functiondes);
        textk.getChildren().add(f2);
        textk.getChildren().add(f3);
        textk.getChildren().add(f4);
        textk.setAlignment(Pos.CENTER);
        textk.setMinWidth(55);
        textk.setMinHeight(25);
        Hmessage.getChildren().add(top);
        top.add(textk, 0, 1, 1, 1);
        HBox Hgetinput = new HBox();//左下的输入框
        GridPane TEMP = new GridPane();
        TEMP.setMaxWidth(520);
        HBox HSEND = new HBox();
        HSEND.setAlignment(Pos.CENTER_RIGHT);
        HSEND.getChildren().add(send);
        TEMP.add(inputarea, 0, 0, 1, 1);
        TEMP.add(HSEND, 0, 1, 1, 1);
        Hgetinput.getChildren().add(TEMP);
        Background hm = new Background(new BackgroundFill(Color.rgb(245, 245, 245), CornerRadii.EMPTY, Insets.EMPTY));
        Hmessage.setBackground(hm);
        BorderPane borderPane = new BorderPane();//自由布局
        Scene scene = new Scene(borderPane, 520, HEIGH);
        Interface(Hmessage, top, textk, Hgetinput, scene, getlist);
        GridPane bt = new GridPane();
        bt.add(Hgetinput, 0, 0, 1, 1);
        /**
         * 这里是最右边的文本框,打印后台输出消息
         */
        borderPane.setLeft(Hmessage);
        borderPane.setBottom(bt);
        onepeople.setScene(scene);
        Thread receive = new receiveone(Client, title,getdata,getlist,mess);//title就是用户信息了
        receive.start();
        Thread showvoice = new showvoice(getdata,getlist,title);
        showvoice.start();
        getlist.getSelectionModel().selectedItemProperty().addListener(
                (ObservableValue<? extends Object> ov, Object old_val,
                 Object new_val) -> {
                    if (new_val != null) {
//                        System.out.println(new_val);
                        if (String.valueOf(new_val).contains("done\\")&&String.valueOf(new_val).contains("fzvoice"))
                        {
                            try {
                                infarea.appendText("播放录音");
//                                File file = new File(String.valueOf(new_val));
                                new Thread(()->play(String.valueOf(new_val))).start();
                            }catch (Exception e)
                            {
                                infarea.appendText("不是录音,不允许点击");
                            }
                        }
                    }
                });
        onepeople.setOnCloseRequest(event -> {
            titlelsit.remove(title);
            receive.stop();
            showimage.stop();
            showvoice.stop();
        });
        scene.widthProperty().addListener((observable, oldValue, newValue) -> {
            getlist.setMinWidth((double)newValue);
            inputarea.setMinWidth((double)newValue);
        });
        onepeople.setMinWidth(520);
        File ico = new File("ico\\solomessage.png");
        try {
            onepeople.getIcons().add(new Image(new FileInputStream(ico)));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        onepeople.show();
    }
}
