package net.kmountain;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * HTTPSプロトコルだとSteamがブラウザで開かれるので直接Steamアプリに飛ぶようにURLを変換して自動的にDiscordに投稿するbot<br>
 * 基本の設定は「discord bot」でぐぐれ
 *
 * @author k.yamamoto
 *
 */
public class SteamURLConverter extends ListenerAdapter {

	private static JDA jda;
	private static String TOKEN; // 取得したBotのトークン

	public static void main(String args[]) throws LoginException {
		extracted();

	}

	private static void extracted() throws LoginException {
		try {
			// 設定ファイルからTOKENを取得(resourceフォルダ内のbot.propertiesから読み込み)

			Properties properties = new Properties();
			FileInputStream fi = new FileInputStream("bot.properties");
			properties.load(fi);
			TOKEN = properties.getProperty("token");

			// BOTのステータスを設定
			jda = JDABuilder.createDefault(TOKEN).build();

			// BOTにイベントリスナを登録
			jda = JDABuilder.createDefault(TOKEN).addEventListeners(new SteamURLConverter()).build();

			// jdaの接続が完了するまで待機シャトル
			jda.awaitReady();

		} catch (InterruptedException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

	/**
	 *
	 * Discordに投稿されたメッセージに反応する。
	 * その際SteamのURLが含まれていた場合、Steamアプリで直接開くURLに変換して同一チャンネルに投稿する。
	 *
	 * @author k.yamamoto
	 *
	 * @param event MessageReceivedEvent
	 */
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {

		String ragex = "https://.+steampowered.com.+[0-9]{1,8}.*/";
		Pattern p = Pattern.compile(ragex);
		// 投稿されたメッセージの内容だけを抽出
		String msgContentRaw = event.getMessage().getContentRaw();
		Matcher match = p.matcher(msgContentRaw);
		Convert cvt;
		if (event.isFromType(ChannelType.TEXT)) {
			if (event.getAuthor().equals(jda.getSelfUser())) {
			} else {
				if (match.find()) {
					// デバッグ用
					// System.out.println(msgContentRaw);
					// メッセージの内容からURLのみを抽出
					System.out.println(msgContentRaw.substring(match.start(), match.end()));
					// メッセージをSteamアプリで開くURLにコンバート
					cvt = new Convert(msgContentRaw.substring(match.start(), match.end()));
					// メッセージをチャンネルに送信
					event.getTextChannel().sendMessage(cvt.getConvertedURL()).queue();
				}
			}

		} else if (event.isFromType(ChannelType.VOICE)) {
			// 機能追加予定なし。
			System.out.println("ボイスチャンネル");
		}

	}

}
