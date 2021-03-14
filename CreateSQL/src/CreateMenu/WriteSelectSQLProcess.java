package CreateMenu;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WriteSelectSQLProcess {

	// ファイル作成場所ディレクトリ
	private StringBuilder fsb = null;
	// ファイル名格納
	private StringBuilder fileName = null;

	// SQL作成用検索項目リスト
	private List<String> createSqlkomoku = null;
	// SQL作成用テーブル名称リスト
	private List<String> createSqlTable = null;
	// SQL作成用検索条件リスト
	private List<String> createSqlJoken = null;
	
	// 改行コード定義
    private String zero_d_zero_a = "\r\n";

	public boolean paramCheck(Map sqlmap) {
		String komoku = (String)sqlmap.get("SKOMOKU");
		String table = (String)sqlmap.get("STABLE");
		String joken = (String)sqlmap.get("SJOKEN");

		if(komoku.equals(null) || komoku.equals("")) {
			// エラー処理
			return false;
		} else {}
		if (table.equals(null) || table.equals("")) {
			// エラー処理
			return false;
		} else {}
		if (joken.equals(null) || joken.equals("")) {
			// エラー処理
			return false;
		} else {}

		// 検索項目チェック処理
		boolean komokuFlg = checkKomoku(komoku);
		if (komokuFlg) {
			// 処理を継続
		} else {
			//　項目取得処理失敗
		}
		//　検索テーブルチェック処理
		boolean tableFlg = checkTable(table);
		if(tableFlg) {
			// 処理を継続
		} else {
			// テーブル名取得処理失敗
		}
		//　検索条件チェック処理
		boolean jokenFlg = checkJoken(joken);
		if (jokenFlg) {
			//処理を継続
		} else {
			// 検索条件取得処理失敗
		}
		return true;
	}

	/****************************
	 * 
	 * 検索項目パラメータチェック処理
	 *(SELECT句)
	 ***************************/
	private boolean checkKomoku(String komokuVal) {

		if (komokuVal.equals(null) || komokuVal.equals("")) {
			//　エラー処理
			return false;
		} else {
			// SQL作成用検索項目リスト
			createSqlkomoku = new ArrayList<String>();
			// 項目指定がない場合
			if (komokuVal.equals("*")) {
				createSqlkomoku.add("*");
			} else {
				System.out.println("SELECT分割前：" + komokuVal);
				// 取得した項目を コロンで区切る
				String[] oneStr = komokuVal.split(":");
				// 検索項目リストへ格納
				for (int i=0; i<oneStr.length; i++) {
					createSqlkomoku.add(oneStr[i]);
				}
			}
			return true;
		}
	}
	
	/*******************************
	 * 検索テーブルパラメータチェック処理
	 * (FROM句)
	 * *****************************/
	private boolean checkTable(String tableVal) {
		
		if (tableVal.equals(null) || tableVal.equals("")) {
			//　エラー処理
			return false;
		} else {
			// SQL作成用検索項目リスト
			createSqlTable = new ArrayList<String>();
			// 項目指定がない場合
			if (tableVal.equals("*")) {
				createSqlTable.add("*");
			} else {
				System.out.println("TABLE分割前：" + tableVal);
				// 取得した項目を カンマで区切る
				String[] twoStr = tableVal.split(",");
				// 検索項目リストへ格納
				for (int i=0; i<twoStr.length; i++) {
					createSqlTable.add(twoStr[i]);
				}
			}
			return true;
		}		
	}
	
	/**************************
	 * 検索条件パラメータチェック処理
	 * (WHERE句)
	 * *************************/
	private boolean checkJoken(String jokenVal) {
		
		if (jokenVal.equals(null) || jokenVal.equals("")) {
			//　エラー処理
			return false;
		} else {
			// SQL作成用検索項目リスト
			createSqlJoken = new ArrayList<String>();
			// 項目指定がない場合
			if (jokenVal.equals("*")) {
				createSqlJoken.add("*");
			} else {
				System.out.println("WHERE分割前：" + jokenVal);
				// 取得した項目を コロンで区切る
				String[] treeStr = jokenVal.split(":");
				// 検索項目リストへ格納
				for (int i=0; i<treeStr.length; i++) {
					createSqlJoken.add(treeStr[i]);
				}
			}
			return true;
		}		
	}

	/*********************
	 * SQLファイル作成処理
	 * ********************/
	public void createFile() {
		try {
			fsb = new StringBuilder();
			fileName = new StringBuilder();

			// ファイル名作成（日時をそのままファイル名にすれば重複はしないはず）
			LocalDateTime date = LocalDateTime.now();
			DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
			String fDate = df.format(date);
			fileName.append(fDate);
			fileName.append(".sql");

			// 新規作成ファイル名
			String createFile = fileName.toString();

			// ファイル名重複チェック処理(日時で命名するから被らないとは思うけど)
			boolean checkFlg = fileCheck(createFile);

			if(checkFlg) {
				// 重複した場合、さらに複雑な名前をつける
				StringBuilder newFileName = new StringBuilder();
				newFileName.append(createFile);
				newFileName.append("C");
			} else {}

			// 格納先Path作成
			fsb.append("C:\\SQLFile格納\\");
			fsb.append(createFile);

			// 新規作成ファイル絶対Path
			String dl = fsb.toString();
			System.out.println("新規作成ファイル（絶対Path）：" + dl);
			File newFile = new File(dl);
			newFile.createNewFile();

			// ファイル入力画面
			writeSqlFile(dl);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	// ファイル名重複チェック処理
	private boolean fileCheck(String checkName) {
		// 格納先のファイル重複チェック処理
		File checkF = new File("C:\\SQLFile格納");
		// ファイル名格納リスト
		List<String> resultList = null;
		// フォルダ内にファイルが存在する場合
		if(checkF.exists()) {	
			File files[] = checkF.listFiles();
			// ファイル名格納リスト
			resultList = new ArrayList<String>();
			
			for(int i=0; i<files.length; i++) {
				// 繰り返しの中で宣言どうだろう
				String margName = files[i].toString();
				resultList.add(margName.replace("C:¥¥SQLFile格納", ""));
			}			
		} else {
			// フォルダ内にファイルが存在しない場合、重複チェックは行わない
			return false;
		}
		
		if (resultList.contains(checkName)) {
			// 重複した場合
			return true;
		} else {
			// 重複無しの場合
			return false;
		}
	}

	// 作成SQL書き出し
	private StringBuilder sqlSb = null;
	//　新規作成ファイル入力処理
	private boolean writeSqlFile(String filePath) {
		try {
			sqlSb = new StringBuilder();

			File writeFile = new File(filePath);
			FileWriter fileWriter = new FileWriter(writeFile);

			// 入力されたテーブル数、SQL文作成
			for (int cnt=0; cnt < createSqlTable.size(); cnt++) {
				sqlSb.setLength(0);
			   // 作成SQLカウント数
				int j=0;
				
				sqlSb.append("SELECT ");
				if(createSqlkomoku.get(0).equals("*")) {
					sqlSb.append("*");
				} else {
					sqlSb.append(createSqlkomoku.get(cnt));
					sqlSb.append(",");						
				}
				// 末尾のカンマを削除
				sqlSb.setLength(sqlSb.length()-1);
				sqlSb.append(" ");
	
				// From句作成
				sqlSb.append("FROM ");	
				// テーブル名を設定
				sqlSb.append(createSqlTable.get(cnt));
				sqlSb.append(" ");
				
				// Where句作成
				sqlSb.append("WHERE ");
				sqlSb.append(createSqlJoken.get(cnt));
	
				String resultSql = sqlSb.toString();
				
				fileWriter.write(resultSql);
				j++;
				// 末尾処理
				fileWriter.write(";");
				// 改行コード
				fileWriter.write(zero_d_zero_a);
			}
			fileWriter.close();

		} catch(IOException e) {
			e.printStackTrace();
		}
		return false;
	}
}
