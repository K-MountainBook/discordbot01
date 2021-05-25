package net.kmountain;

/**
 * httpsで始まるURLをSteamアプリでワンクリックで開けるようにコンバートする。
 *
 * @author keita.y
 *
 */
public class Convert {

	private String baseURL;
	private String steamOpenURLPrefix = "steam://openurl/";
	private String convertedURL;

	/**
	 * コンストラクタ
	 *
	 * @param baseURL httpsで始まるsteampoweredのURL
	 */
	public Convert(String baseURL) {
		//		this.name = "convert";
		//		this.help = "httpsで始まるURLをSteamアプリでワンクリックで開けるようにコンバートする。";
		this.baseURL = baseURL;
		// URLにprefixをつける
		this.convertedURL = steamOpenURLPrefix + this.baseURL;
	}

	/**
	 * 編集したURLを返す
	 *
	 * @author k.yamamoto
	 * @return
	 */
	public String getConvertedURL() {
		return this.convertedURL;
	}

}
