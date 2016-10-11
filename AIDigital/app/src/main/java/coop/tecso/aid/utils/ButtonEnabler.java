package coop.tecso.aid.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

/**
 * Handle validations to enable buttons on the screen. 
 */
public class ButtonEnabler implements TextWatcher {
	
	private Button mTargetButton;
	private EditText[] mEditTexts;

	// Constructor
	private ButtonEnabler() { }

	/**
	 * Registers a ButtonEnabler for the target button.
	 * 
	 * By default, this implementation will enable the button 
	 * when the method checkEditText returns true for all
	 * the editText views.
	 */
	public static void register(Button targetButton, EditText... editTexts) {
		ButtonEnabler be = new ButtonEnabler();
		be.mTargetButton = targetButton;
		be.mEditTexts = editTexts;    		

		// Set up the listeners
		for (EditText editText: editTexts) {
			editText.addTextChangedListener(be);
		}
		be.updateButtonState();
	}
	
	/**
	 * Validation rule to perform in all the edit texts.
	 * 
	 * By the default, returns true if and only if
	 * the editText text is not empty.
	 * 	 
	 * Override for custom validations.
	 */
	public boolean checkEditText(EditText edit) {			
		return edit.getText().length() > 0;
	}

	@Override
	public void afterTextChanged(Editable s) {
        updateButtonState();
    }

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before,
			int count) {
		// TODO Auto-generated method stub
	}
	    
	// Implementation helpers
	
	private void updateButtonState() {
		mTargetButton.setEnabled(canEnableButton());
	}

	private boolean canEnableButton() {			
		for (EditText edit: mEditTexts) {
			if (!checkEditText(edit)) 
				return false;
		} 
		return true;    		
	}
}