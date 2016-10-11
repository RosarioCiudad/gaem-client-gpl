package coop.tecso.aid.gui.components;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import coop.tecso.aid.R;
import coop.tecso.aid.activities.RegisterActivity;
import coop.tecso.aid.gui.helpers.Value;
import coop.tecso.aid.helpers.GUIHelper;
import coop.tecso.aid.helpers.ParamHelper;
import coop.tecso.daa.domain.perfil.AplPerfilSeccionCampo;
import coop.tecso.daa.domain.perfil.AplPerfilSeccionCampoValor;
import coop.tecso.daa.domain.perfil.AplPerfilSeccionCampoValorOpcion;

/**
 * 
 * @author tecso.coop
 *
 */
public class PhotoGUI extends CampoGUI {

	private final String LOG_TAG = PhotoGUI.class.getSimpleName();
  
	private LinearLayout mainLayout;
	private ImageButton  btnAttach;
	private ImageView 	 mImageView;
	private EditText 	 textBox;
	private byte[] 		 data;

	public PhotoGUI(Context context) {
		super(context);
	}

	public PhotoGUI(Context context, boolean enabled) {
		super(context, enabled);
	}

	public PhotoGUI(Context context, List<Value> values) {
		super(context,values);
	}

	// Metodos
	@Override
	public View build() {
		// Etiqueta
		this.label = new TextView(context);
		this.label.setTextColor(context.getResources().getColor(R.color.label_text_color));
		this.label.setText(this.getEtiqueta()+": ");
		this.label.setPadding(0, 10, 0, 15); 
		this.mImageView = new ImageView(context);
		this.textBox = new EditText(context);

		if(appState.getOrientation() == LinearLayout.VERTICAL){
			// Se define un LinearLayout para ubicar: 'Label / EditText'
			this.mainLayout = new LinearLayout(context);
			this.mainLayout.setOrientation(LinearLayout.VERTICAL);
			this.mImageView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		}else{
			//Se define un Table Row para ubicar: 'Label | EditText'
			this.mainLayout = new TableRow(context);
			this.label.setGravity(Gravity.RIGHT);
			this.label.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 3f));
			this.mImageView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, 1f));
		}
		this.mainLayout.setBackgroundColor(context.getResources().getColor(R.color.default_background_color));
		this.mainLayout.setGravity(Gravity.CENTER_VERTICAL);

		// Attached image
		this.textBox.setEnabled(enabled);
		this.textBox.setFocusable(enabled);
		if(this.getInitialValues() != null && this.getInitialValues().size() == 1){
			this.textBox.setText(this.getInitialValues().get(0).getValor());
			this.data = this.getInitialValues().get(0).getImagen();
			if(null == data){
				this.mImageView.setImageResource(R.drawable.no_image);
			}else{
				Bitmap thumbnail = BitmapFactory.decodeByteArray(data, 0, data.length);
				this.mImageView.setImageBitmap(thumbnail);
			}
		}else{
			this.textBox.setText(this.getValorDefault());
			this.mImageView.setImageResource(R.drawable.no_image);
		}
		

		this.btnAttach = new ImageButton(context);
		this.btnAttach.setEnabled(enabled);
		this.btnAttach.setFocusable(enabled);
		this.btnAttach.setBackgroundResource(android.R.drawable.ic_menu_camera);
		this.btnAttach.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		this.btnAttach.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Marco el campo como selected asi el activity sabe cual adjunto refrescar
				perfilGUI.setCampoGUISelected(PhotoGUI.this);

				//Creamos el archivo donde guardar la foto
				File imegeDirectoryPrivate = context.getExternalFilesDir(null);

				//System.out.println("PATH imegeDirectory: "+imegeDirectoryPrivate.getAbsolutePath()); 
				File image = new File(imegeDirectoryPrivate, "PIC_.jpeg");
				if(image.exists()){
					image.delete();
				}
				Uri uriSavedImage = Uri.fromFile(image);
				//System.out.println("PATH uri creado: "+uriSavedImage.getPath()); 

				//creamos el Intent
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
				((Activity) context).startActivityForResult(intent, RegisterActivity.REQUEST_NEW_PHOTO);
			}
		});

		//Genero layout para poder manipular las imagenes.
		LinearLayout btnLayout = new LinearLayout(context);
		btnLayout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT) );
		btnLayout.setGravity(Gravity.RIGHT);
		btnLayout.setPadding(20, 10, 0, 10);
		btnLayout.addView(btnAttach);

		//Genero layout para poder manipular las imagenes.
		LinearLayout photoLayout = new LinearLayout(context);
		photoLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT) );
		//		photoLayout.setPadding(20, 15, 0, 25); //solo si la foto fue sacada.
		photoLayout.setGravity(Gravity.LEFT);
		photoLayout.addView(mImageView);

		//Linea separadora de imagens.
		View gap = new View(context);
		gap.setBackgroundColor(context.getResources().getColor(R.color.label_text_color));

		RelativeLayout comp = new RelativeLayout(context);

		photoLayout.setId(1);
		btnLayout.setId(2);

		RelativeLayout.LayoutParams lpImage = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		lpImage.addRule(RelativeLayout.ALIGN_LEFT, btnLayout.getId());
		comp.addView(photoLayout, lpImage);

		RelativeLayout.LayoutParams lpBtn = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		lpBtn.addRule(RelativeLayout.ALIGN_RIGHT, -1) ;
		comp.addView(btnLayout, lpBtn);


		this.mainLayout.addView(label);
		this.mainLayout.addView(comp);
		if(enabled){
			this.mImageView.setFocusableInTouchMode(true);
			this.mImageView.requestFocus();
			this.btnAttach.performClick();
		}
		//		this.mainLayout.addView(textBox);
		//		this.textBox.requestFocus();
		this.mainLayout.addView(gap, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2));

		this.view = mainLayout;

		return this.view;
	}

	@Override
	public View redraw() {
		this.btnAttach.setEnabled(enabled);
		this.btnAttach.setFocusable(enabled);
		this.textBox.setEnabled(enabled);
		this.textBox.setFocusable(enabled);
		return this.view;
	}

	@Override
	public List<Value> values() {
		this.values = new ArrayList<Value>();

		AplPerfilSeccionCampo campo = null;
		AplPerfilSeccionCampoValor campoValor = null;
		AplPerfilSeccionCampoValorOpcion campoValorOpcion = null;
		if(this.entity instanceof AplPerfilSeccionCampo){
			campo = (AplPerfilSeccionCampo) this.entity;
		}else if(this.entity instanceof AplPerfilSeccionCampoValor){
			campoValor = (AplPerfilSeccionCampoValor) this.entity;
			campo = campoValor.getAplPerfilSeccionCampo();
		}else if(this.entity instanceof AplPerfilSeccionCampoValorOpcion){
			campoValorOpcion = (AplPerfilSeccionCampoValorOpcion) this.entity;
			campoValor = campoValorOpcion.getAplPerfilSeccionCampoValor();
			campo = campoValor.getAplPerfilSeccionCampo();
		}
		String nombreCampo = campo!=null?campo.getCampo().getEtiqueta():"No identificado";
		String valor = this.textBox.getText().toString();

		Log.d(LOG_TAG ,"save() : "+this.getTratamiento()+" :Campo: "+nombreCampo
				+" idCampo: "+(campo!=null?campo.getId():"null")
				+", idCampoValor: "+(campoValor!=null?campoValor.getId():"null")
				+", idCampoValorOpcion: "+(campoValorOpcion!=null?campoValorOpcion.getId():"null")
				+", Valor: "+valor);

		Value vData = new Value(campo,campoValor,campoValorOpcion,valor,data);
		this.values.add(vData);

		return this.values;
	}

	@Override
	public boolean isDirty(){
		boolean dirty = false;
		byte[] currentValue = data;
		if(getInitialValues() != null && getInitialValues().size() > 0){
			byte[] initialValue = getInitialValues().get(0).getImagen();
			dirty = !Arrays.equals(currentValue, initialValue);
		}else {
			dirty = currentValue != null;
		}
		if(dirty){
			Log.d(LOG_TAG, String.format("%s isDirty: true", getEtiqueta()));
		}
		return dirty;
	}

	@Override
	public boolean validate() {
		if(isObligatorio() && data.length == 0 && TextUtils.isEmpty(this.textBox.getText().toString())){
			GUIHelper.showError(context, context.getString(R.string.field_required, getEtiqueta()));
			mImageView.requestFocus();

			return false;
		}
		return true;
	}

	public void setImageBitmap() {
		File imageDir = context.getExternalFilesDir(null);
		File photo = null;
		for (File temp : imageDir.listFiles()) {
			if (temp.getName().equals("PIC_.jpeg")) {
				imageDir = temp;
				photo = new File(context.getExternalFilesDir(null), "PIC_.jpeg");
				//pic = photo;
				break;
			}
		}
		if(photo != null) {
			// Read Bitmap
			BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
			//bitmapOptions.inJustDecodeBounds = true;
			Bitmap imageBitmap = BitmapFactory.decodeFile(photo.getAbsolutePath(), bitmapOptions);
			//achicamos la imagen
			int originalHeight = imageBitmap.getHeight(); //dimensiones originales
			int originalWidth = imageBitmap.getWidth();
			
			int targetWidth = ParamHelper.getInteger(ParamHelper.RESOLUCION, 640);//640; //fijamos el ancho a la que queremos fijar la imagen y calculamos el largo a partir de el (para no deformarla)
			int targetHeight = (int) (originalHeight * targetWidth / (double) originalWidth);
			
			bitmapOptions.inSampleSize = calculateInSampleSize(bitmapOptions, targetWidth, targetHeight);
		    // Decode bitmap with inSampleSize set
			bitmapOptions.inJustDecodeBounds = false;
		    Bitmap compressedImage = Bitmap.createScaledBitmap(imageBitmap,targetWidth, targetHeight, true);
		    
			//-----------------------------------------
			//this.mImageView.setImageBitmap(compressedImage);
			this.mImageView.setImageBitmap(compressedImage);
			this.mImageView.setAdjustViewBounds(true);

			//guardamos el BitMap en formato JPEG
			try{
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				compressedImage.compress(Bitmap.CompressFormat.JPEG, 60, stream);
				this.data = stream.toByteArray();
				
				File tempReduceImage = context.getExternalFilesDir(null);

				//guardamos la imagen en la memoria del telefono para pruebas
				File imageReduce = new File(tempReduceImage, "PIC_resized"+java.util.UUID.randomUUID()+".jpg");
				FileOutputStream out = new FileOutputStream(imageReduce);
				compressedImage.compress(Bitmap.CompressFormat.JPEG, 70, out);

				// Borramos el archivo
				photo.delete();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void setImageBitmap(Bitmap imageBitmap) {
		this.mImageView.setImageBitmap(imageBitmap);

		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
		this.data = stream.toByteArray();
	}


	@Override
	public void removeAllViewsForMainLayout(){
		this.mainLayout.removeAllViews();
	}

	@Override
	public String getValorView() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Calcuate how much to compress the image
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}
		return inSampleSize;
	}
}
