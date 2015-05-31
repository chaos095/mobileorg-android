package com.matburt.mobileorg.Gui.Wizard.Wizards;

import com.matburt.mobileorg.R;
import com.matburt.mobileorg.Gui.Wizard.WizardView;
import com.matburt.mobileorg.util.MOUserInfo;

import android.app.AlertDialog;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public abstract class Wizard {

	protected Context context;
	protected WizardView wizardView;

	protected UIHandler uiHandler;
	protected ProgressDialog progress;

	public Wizard(WizardView wizardView, Context context) {
		this.context = context;
		this.wizardView = wizardView;

		progress = new ProgressDialog(context);
		progress.setMessage(context.getString(R.string.please_wait));
		progress.setTitle(context.getString(R.string.signing_in));

		wizardView.removePagesAfter(1);
		setupFirstPage();
	}

	public void setupDoneButton(View view) {
		Button doneButton = (Button) view.findViewById(R.id.wizard_done_button);
		doneButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				saveSettings();
				((Activity) context).finish();
			}
		});
	}

	public abstract void setupFirstPage();

	public void refresh() {
	}

	public final class UIHandler extends Handler {
		public static final int DISPLAY_UI_TOAST = 0;
		public static final int DISPLAY_UI_ALERT = 1;

		public UIHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DISPLAY_UI_TOAST: {
				Toast t = Toast.makeText(context, (String) msg.obj,
						Toast.LENGTH_LONG);
				progress.dismiss();
				t.show();
				break;
			}
			case DISPLAY_UI_ALERT: {
			        final MOUserInfo ui = (MOUserInfo) msg.obj;
			        progress.dismiss();
				new AlertDialog.Builder(context)
				    .setTitle("Unknown public key")
				    .setMessage(ui.getMessage())
				    .setIcon(android.R.drawable.ic_dialog_alert)
				    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
					    public void onClick(DialogInterface dialog, int whichButton) {
						ui.confirmNewKey();
					    }})
				    .setNegativeButton(android.R.string.no, null)
				    .show();
				break;
			}
			default:
				break;
			}
		}
	}

	public void showToastRemote(String message) {
		Message msg = uiHandler.obtainMessage(UIHandler.DISPLAY_UI_TOAST);
		msg.obj = message;
		uiHandler.sendMessage(msg);
	}

    	public void showKeyAlertRemote(MOUserInfo ui) {
		Message msg = uiHandler.obtainMessage(UIHandler.DISPLAY_UI_ALERT);
		msg.obj = ui;
		uiHandler.sendMessage(msg);
	}

	public abstract void saveSettings();

	public enum TYPE {
		WebDAV, Dropbox, Ubuntu, SDCard, SSH, Null
	};

	public static Wizard getWizard(TYPE type, WizardView wizardView,
			Context context) {
		Wizard wizard;

		switch (type) {
		case WebDAV:
			wizard = new WebDAVWizard(wizardView, context);
			break;

		case Dropbox:
			wizard = new DropboxWizard(wizardView, context);
			break;

		case Ubuntu:
			wizard = new UbuntuOneWizard(wizardView, context);
			break;

		case SDCard:
			wizard = new SDCardWizard(wizardView, context);
			break;

		case SSH:
			wizard = new SSHWizard(wizardView, context);
			break;

		case Null:
			wizard = new NullWizard(wizardView, context);
			break;

		default:
			wizard = new NullWizard(wizardView, context);
			break;
		}

		return wizard;
	}
}
