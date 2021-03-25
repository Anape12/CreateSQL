package main;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import CreateMenu.WriteSelectSQLProcess;
import parts.EventBeginning;
import parts.ListenerInterface;

/*
 * 前提として、C直下に「SQLFile格納」というフォルダを作っておくこと
 * */

public class CreateSQLFile extends JFrame implements ActionListener,ListenerInterface{

	// 検索SQL作成ラベル
	private static JLabel selectLabel = null;
	// 更新SQL作成ラベル
	private static JLabel updateLabel = null;
	// 追加SQL作成ラベル
	private static JLabel insertLabel = null;
	// 削除SQL作成ラベル
	private static JLabel deleteLabel = null;

	// ボタンイベント取得
	private static EventBeginning childClass = null;
	private static CreateSQLFile csqlF = null;

	private static JPanel cardPanel = null;
	private static CardLayout layout = null;

	public static String SELECT_MENU = "検索SQL作成画面";
	public static String UPDATE_MENU = "更新SQL作成画面";
	public static String INSERT_MENU = "追加SQL作成画面";
	public static String DELETE_MENU = "削除SQL作成画面";
	public static String CONFIG = "設定ボタン";

	//全画面共通「設定」ボタン
	private static JButton enterButton = null;
	/*********************
	 * 検索パネル内、部品項目
	 **********************/
	// 検索SQL作成画面
	private static JPanel selectPanel = null;
	// 検索対象テーブル入力テキスト
	private static JTextField slTbText = null;
	// 検索対象テーブルラベル
	private static JLabel slTbLab = null;
	// 検索項目入力テキスト
	private static JTextField slText = null;
	// テキスト明示用ラベル
	private static JLabel serchLab = null;
	// 検索条件入力テキスト
	private static JTextField sljkText = null;
	// 検索条件明示用ラベル
	private static JLabel sljkLab = null;

	/*********************
	 * 更新SQLパネル内、部品項目
	 **********************/
	// 更新SQL作成画面
	private static JPanel updatePanel = null;
	// 更新項目入力テキスト
	private static JTextField upKoshinText = null;
	// 更新対象テーブル入力テキスト
	private static JTextField upTbText = null;
	// 更新対象条件入力テキスト
	private static JTextField upJokenText = null;
	// テキスト明示用ラベル
	private static JLabel updateLab = null;
	/*********************
	 * 追加SQLパネル内、部品項目
	 **********************/
	// 追加パネル
	private static JPanel insertPanel = null;
	/*********************
	 * 削除SQLパネル内、部品項目
	 **********************/
	// 削除パネル
	private static JPanel deletePanel = null;

	// SQLファイル作成種処理インスタンス
	private WriteSelectSQLProcess wsql = null;

	// SQLファイル作成パターン（初期値は検索画面）
	private static String sqlPattern = "SA";

	// 入力画面出力初期処理
	CreateSQLFile(){
		enterButton = new JButton("設定");

		createSelectMenu();

		createUpdateMenu();

		createInsertMenu();

		createDeleteMenu();

		cardPanel = new JPanel();
		layout = new CardLayout();
		cardPanel.setLayout(layout);

		cardPanel.add(selectPanel, SELECT_MENU);
		cardPanel.add(updatePanel, UPDATE_MENU);
		cardPanel.add(insertPanel, INSERT_MENU);
		cardPanel.add(deletePanel, DELETE_MENU);

		//
		JButton slButton = new JButton(SELECT_MENU);
		slButton.addActionListener(this);
		slButton.setActionCommand(SELECT_MENU);

		//
		JButton upButton = new JButton(UPDATE_MENU);
		upButton.addActionListener(this);
		upButton.setActionCommand(UPDATE_MENU);

		//
		JButton inButton = new JButton(INSERT_MENU);
		inButton.addActionListener(this);
		inButton.setActionCommand(INSERT_MENU);

		//
		JButton dlButton = new JButton(DELETE_MENU);
		dlButton.addActionListener(this);
		dlButton.setActionCommand(DELETE_MENU);

		JPanel btnPanel = new JPanel();
		btnPanel.add(slButton);
		btnPanel.add(upButton);
		btnPanel.add(inButton);
		btnPanel.add(dlButton);

		//
		Container contentPane = getContentPane();
		contentPane.add(cardPanel, BorderLayout.CENTER);
		contentPane.add(btnPanel, BorderLayout.PAGE_END);

		//
		childClass = new EventBeginning();
		childClass.addListener(this);
		//
		enterButton.addActionListener(this);
		enterButton.setActionCommand(CONFIG);
	}

	public static void main(String[] args) {
		csqlF = new CreateSQLFile();
		csqlF.setTitle("作成したいSQLファイルを選択");
		csqlF.setSize(900,600);
		csqlF.setLocationRelativeTo(null);
		csqlF.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		csqlF.setVisible(true);
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if(cmd.equals(SELECT_MENU)) {
			System.out.println("検索SQL作成画面へ移行");
			layout.show(cardPanel, cmd);
		}
		else if (cmd.equals(UPDATE_MENU)) {
			System.out.println("更新SQL作成画面へ移行");
			layout.show(cardPanel, cmd);
		}
		else if (cmd.equals(INSERT_MENU)) {
			System.out.println("追加SQL作成画面へ移行");
			layout.show(cardPanel, cmd);
		}
		else if (cmd.equals(DELETE_MENU)) {
			System.out.println("削除SQL作成画面へ移行");
			layout.show(cardPanel, cmd);
		}
		else if (cmd.equals(CONFIG)) {
			childClass.eventListener();
		}
		else {}
	}

	// 検索SQLファイル作成画面
	private static void createSelectMenu() {
		System.out.println("createSelectMenu");
		// 検索SQL作成画面
		selectPanel = new JPanel();
		selectLabel = new JLabel("<html>" + SELECT_MENU + "<¥n></html>");
		// 項目明示ラベル
		serchLab = new JLabel("抽出項目：");
		// 検索項目入力テキスト
		slText = new JTextField();
		slText.setPreferredSize(new Dimension(600,30));
		// テーブル数明示ラベル
		slTbLab = new JLabel("対象テーブル：");
		// テーブル数入力テキスト
		slTbText = new JTextField();
		slTbText.setPreferredSize(new Dimension(600,30));
		JLabel l1 = new JLabel("<hmlt>¥n</html>");
		// 検索条件明示テキスト
		sljkLab = new JLabel("検索条件：");
		// 検索条件入力テキスト
		sljkText = new JTextField();
		sljkText.setPreferredSize(new Dimension(600,30));
		JLabel l2 = new JLabel("<html>¥n</hmlt>");
		// 以下、画面作成処理
		selectPanel.add(selectLabel);
		selectPanel.add(serchLab);
		selectPanel.add(slText);
		selectPanel.add(slTbLab);
		selectPanel.add(slTbText);
		selectPanel.add(l1);
		selectPanel.add(sljkLab);
		selectPanel.add(sljkText);
		selectPanel.add(l2);
		selectPanel.add(enterButton);
	}

	//　更新SQLファイル作成画面
	private static void createUpdateMenu() {
		//
		updatePanel = new JPanel();
		upKoshinText = new JTextField();
		upTbText = new JTextField();
		upJokenText = new JTextField();
		updateLab = new JLabel("更新項目数：");
		upKoshinText.setPreferredSize(new Dimension(600,30));
		updatePanel.setBackground(Color.LIGHT_GRAY);
		updateLabel = new JLabel(UPDATE_MENU);
		upKoshinText.setPreferredSize(new Dimension(600,30));
		upJokenText.setPreferredSize(new Dimension(600,30));
		enterButton = new JButton("設定");
		updatePanel.add(upKoshinText);
		updatePanel.add(updateLab);
		updatePanel.add(upTbText);
		updatePanel.add(upJokenText);
		updatePanel.add(enterButton);
	}
	//　追加SQLファイル作成画面
	private static void createInsertMenu() {
		insertPanel = new JPanel();
		insertPanel.setBackground(Color.CYAN);
		insertLabel = new JLabel(INSERT_MENU);
		insertPanel.add(insertLabel);
	}
	//　削除SQLファイル作成画面
	private static void createDeleteMenu() {
		deletePanel = new JPanel();
		deletePanel.setBackground(Color.GREEN);
		deleteLabel = new JLabel(DELETE_MENU);
		deletePanel.add(deleteLabel);
	}

	// 設定ボタン押下後の処理
	@Override
	public void buttonAction() {
		// TODO 自動生成されたメソッド・スタブ
		System.out.println("検索項目：「" + slText.getText() + "」");
		System.out.println("対象テーブル：「" + slTbText.getText() + "」");
		System.out.println("検索条件：「" + sljkText.getText() + "」");

		// 検索項目格納リスト
		String komokuVal = null;
		// テーブル名格納リスト
		String tableVal = null;
		// 検索条件格納リスト
		String jokenVal = null;

		if (sqlPattern.equals("SA")) {
			// 検索項目格納リスト
			komokuVal = slText.getText();
			// テーブル名格納リスト
			tableVal = slTbText.getText();
			// 検索条件格納リスト
			jokenVal = sljkText.getText();

			Map<String,String> sqlmap = new HashMap();
			sqlmap.put("SKOMOKU", komokuVal);
			sqlmap.put("STABLE", tableVal);
			sqlmap.put("SJOKEN", jokenVal);

			// SQLファイル作詞　種処理開始
			wsql = new WriteSelectSQLProcess();
			// テキスト入力値チェック処理
			boolean checkFlg = wsql.paramCheck(sqlmap);
			if(checkFlg) {
				wsql.createFile();
			}
		}
		else if(sqlPattern.equals("UA")) {
			// 項目格納リスト
			komokuVal = upKoshinText.getText();
			// テーブル名格納リスト
			tableVal = upTbText.getText();
			// 条件格納リスト
			jokenVal = upJokenText.getText();
		}
		else if(sqlPattern.equals("IA")) {

		}
		else if(sqlPattern.equals("DA")) {

		}
	}
}