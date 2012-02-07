package com.paypal.ipn;

import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.android.c2dm.C2DMBaseReceiver;

public class C2DMReceiver extends C2DMBaseReceiver {
	public C2DMReceiver() {
		super("brasil@x.com");
	}

	/**
	 * Recebe o id de registro e envia para a nossa aplicação no servidor, que
	 * manipulará as notificações instantânea de pagamento.
	 */
	@Override
	public void onRegistered(Context context, String registrationId)
			throws java.io.IOException {

		SharedPreferences prefs = context.getSharedPreferences(
				context.getString(R.string.app_id), Context.MODE_PRIVATE);

		URL url = new URL(context.getString(R.string.register_server));

		URLConnection conn = url.openConnection();

		StringBuilder sb = new StringBuilder();

		sb.append("registrationId=" + registrationId);
		sb.append("&receiverEmail=" + prefs.getString("receiverEmail", ""));

		conn.setDoOutput(true);

		OutputStreamWriter writer = new OutputStreamWriter(
				conn.getOutputStream());

		writer.write(sb.toString());
		writer.flush();
		writer.close();

		Editor editor = prefs.edit();

		editor.putString("registrationId", registrationId);
		editor.commit();
	};

	/**
	 * Mensagem recebida!
	 */
	@Override
	protected void onMessage(Context context, Intent intent) {
		String ns = Context.NOTIFICATION_SERVICE;

		// Pegamos o gerenciados de notificação
		NotificationManager notificationManager = (NotificationManager) getSystemService(ns);

		// Ícone que aparecerá na notificação
		int icon = R.drawable.ic_launcher;
		
		// Mensagem que aparecerá quando a notificação aparecer.
		CharSequence tickerText = context
				.getString(R.string.ipn_notification_ticker);
		long when = System.currentTimeMillis();

		// Criamos a notificação, definindo o título, texto, som, etc
		Notification notification = new Notification(icon, tickerText, when);

		CharSequence contentTitle = context
				.getString(R.string.ipn_notification_title);
		CharSequence contentText = context
				.getString(R.string.ipn_notification_text);
		Intent notificationIntent = new Intent(this, RegisterActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				notificationIntent, 0);

		notification.defaults |= Notification.DEFAULT_SOUND;
		notification.setLatestEventInfo(context, contentTitle, contentText,
				contentIntent);

		// Exibimos a notificação
		notificationManager.notify(1, notification);

	}

	@Override
	public void onError(Context context, String errorId) {
		// opz
	}
}