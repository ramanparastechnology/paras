package com.paras.choo.fragments;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.os.Handler;
import android.provider.MediaStore;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.paras.choo.R;
import com.paras.choo.utils.ChooPref;
import com.paras.choo.utils.ImageHelper2;
import com.paras.choo.utils.ImageHelper3;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URL;
import java.nio.ByteBuffer;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TicketScanFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TicketScanFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TicketScanFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View rootview;
    LinearLayout pickphoto;
    RelativeLayout setphoto;
    int id;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    FrameLayout layout_MainMenu;
    Typeface headlinefont, fieldsfont;
    TextView heading, text1, text2, text3;
    Uri selectedImageUri;
    String  selectedPath;
    String Path;
    URL connectURL;
    //    Animation shake;
    ImageView selectedImage, delete;
    private static int RESULT_LOAD_IMAGE = 1;
    //    private static int RESULT_OK;
    ImageView progressbar, left, right;
    private static final int CAMERA_REQUEST = 1888;
    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter dot1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TicketScanFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TicketScanFragment newInstance(String param1, String param2) {
        TicketScanFragment fragment = new TicketScanFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public TicketScanFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.fragment_ticket_scan, container, false);
        headlinefont = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Medium.ttf");
        fieldsfont = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Light.ttf");
        heading = (TextView)rootview.findViewById(R.id.heading);
        text1 = (TextView)rootview.findViewById(R.id.text1);
        text2 = (TextView)rootview.findViewById(R.id.text2);
        text3 = (TextView)rootview.findViewById(R.id.text3);
        layout_MainMenu = (FrameLayout) getActivity().findViewById( R.id.realtabcontent);
        layout_MainMenu.getForeground().setAlpha(0);
        left = (ImageView)rootview.findViewById(R.id.left);
        right = (ImageView)rootview.findViewById(R.id.right);
        pickphoto = (LinearLayout)rootview.findViewById(R.id.pickphoto);
        setphoto = (RelativeLayout)rootview.findViewById(R.id.setphoto);
        progressbar = (ImageView)rootview.findViewById(R.id.progress5);
        selectedImage = (ImageView)rootview.findViewById(R.id.selectedimage);
        prefs = getActivity().getSharedPreferences(ChooPref.CHOO_PREF,getActivity().MODE_PRIVATE);
        editor = getActivity().getSharedPreferences(ChooPref.CHOO_PREF,getActivity().MODE_PRIVATE).edit();

        delete = (ImageView)rootview.findViewById(R.id.delete);

        heading.setTypeface(headlinefont);
        text1.setTypeface(fieldsfont);
        text2.setTypeface(fieldsfont);
        text3.setTypeface(fieldsfont);
        if(prefs.getString(ChooPref.TICKET_IMAGE,null) != null)
        {
            pickphoto.setVisibility(View.GONE);
            setphoto.setVisibility(View.VISIBLE);
            if( !prefs.getString(ChooPref.TICKET_IMAGE,null).equalsIgnoreCase("") ){
                byte[] b = Base64.decode(prefs.getString(ChooPref.TICKET_IMAGE, null), Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                selectedImage.setImageBitmap(bitmap);
            }

        }
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (setphoto.getVisibility() == View.VISIBLE) {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    OverviewFragment fragment = new OverviewFragment();
                    fragmentTransaction.replace(R.id.realtabcontent, fragment, "Overview").addToBackStack(null);
                    fragmentTransaction.commit();
                } else {
//                    rootview.findViewById(R.id.pickphoto).startAnimation(shake);
                    showCautionPopup(getActivity());
                    layout_MainMenu.getForeground().setAlpha(220); // dim
                }

            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickphoto.setVisibility(View.VISIBLE);
                setphoto.setVisibility(View.GONE);
            }
        });
        pickphoto.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            @Override
            public void onClick(View v) {
                final PopupMenu popupMenu = new PopupMenu(getActivity(),v);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.gallery:
                                id = 0;
                                Intent i = new Intent(
                                        Intent.ACTION_PICK,
                                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(i, RESULT_LOAD_IMAGE);
                                return true;
                            case R.id.camera:
                                id = 1;
                                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(cameraIntent, CAMERA_REQUEST);
                                return true;
                            case R.id.cancel:
                                popupMenu.dismiss();
                                return true;
                        }
                        return false;
                    }
                });
                popupMenu.inflate(R.menu.photopopup);
                popupMenu.show();
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressbar.setImageResource(R.mipmap.progress_bar6);
            }
        },100);
        return rootview;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_ticket_scan_info, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    private void showCautionPopup(final Activity context) {
        // Inflate the load_last_data_popup_layoutta_popup_layout.xml
        RelativeLayout viewGroup = (RelativeLayout) context.findViewById(R.id.popup);
        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.scan_ticket_caution_popup, null);

        // Creating the PopupWindow
        final PopupWindow popup = new PopupWindow(context);
        popup.setContentView(layout);
        popup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popup.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        popup.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        popup.setFocusable(true);
        popup.showAtLocation(layout,
                Gravity.CENTER|Gravity.CENTER, Gravity.CENTER,Gravity.CENTER);
        // Getting a reference to Close button, and close the popup when clicked.
        LinearLayout info = (LinearLayout) layout.findViewById(R.id.warning);
        Button ok = (Button) layout.findViewById(R.id.ok);
        info.bringToFront();
        ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                popup.dismiss();
                layout_MainMenu.getForeground().setAlpha( 0); // restore
            }
        });
    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }
//    http://voidcanvas.com/whatsapp-like-image-compression-in-android/

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap bitmap;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) getActivity().getApplicationContext().getSystemService(Context.WINDOW_SERVICE); // the results will be higher than using the activity context object or the getWindowManager() shortcut
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = 4 * displayMetrics.widthPixels;
        int screenHeight = 4 * displayMetrics.heightPixels;
        Log.e("screenWidth, screenHeight ",screenWidth + " " + screenHeight);
        if(requestCode == RESULT_LOAD_IMAGE){
            try{
                selectedImageUri = data.getData();
                editor.putString(ChooPref.SELECTED_IMAGE_URI, selectedImageUri+"").commit();
                Log.e("selectedImageUri"+selectedImageUri, "c"+getActivity());
                if(selectedImageUri == null)
                    return;
                Path = ImageHelper2.compressImage(selectedImageUri, getActivity());
                pickphoto.setVisibility(View.GONE);
                setphoto.setVisibility(View.VISIBLE);
                selectedImage.setAdjustViewBounds(true);
                bitmap = ImageHelper2.decodeSampledBitmapFromFile(Path,screenWidth,screenHeight);
                selectedImage.setImageBitmap(bitmap);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] b = baos.toByteArray();

                String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                editor.putString(ChooPref.TICKET_IMAGE,encodedImage);
                editor.commit();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        else if(requestCode == CAMERA_REQUEST ){
            try {
                Log.i("TAG", "Inside PICK_FROM_CAMERA");


                // Describe the columns you'd like to have returned. Selecting from
                // the Thumbnails location gives you both the Thumbnail Image ID, as
                // well as the original image ID

                try {
                    Log.i("TAG", "inside Samsung Phones");
                    String[] projection = {
                            MediaStore.Images.Thumbnails._ID, // The columns we want
                            MediaStore.Images.Thumbnails.IMAGE_ID,
                            MediaStore.Images.Thumbnails.KIND,
                            MediaStore.Images.Thumbnails.DATA };
                    String selection = MediaStore.Images.Thumbnails.KIND + "=" + // Select
                            // only
                            // mini's
                            MediaStore.Images.Thumbnails.MINI_KIND;

                    String sort = MediaStore.Images.Thumbnails._ID + " DESC";

                    // At the moment, this is a bit of a hack, as I'm returning ALL
                    // images, and just taking the latest one. There is a better way
                    // to
                    // narrow this down I think with a WHERE clause which is
                    // currently
                    // the selection variable
                    Cursor myCursor = getActivity().managedQuery(
                            MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
                            projection, selection, null, sort);

                    long imageId = 0l;
                    long thumbnailImageId = 0l;
                    String thumbnailPath = "";

                    try {
                        myCursor.moveToFirst();
                        imageId = myCursor
                                .getLong(myCursor
                                        .getColumnIndexOrThrow(MediaStore.Images.Thumbnails.IMAGE_ID));
                        thumbnailImageId = myCursor
                                .getLong(myCursor
                                        .getColumnIndexOrThrow(MediaStore.Images.Thumbnails._ID));
                        thumbnailPath = myCursor
                                .getString(myCursor
                                        .getColumnIndexOrThrow(MediaStore.Images.Thumbnails.DATA));
                    } finally {
                        // myCursor.close();
                    }

                    // Create new Cursor to obtain the file Path for the large image

                    String[] largeFileProjection = {
                            MediaStore.Images.ImageColumns._ID,
                            MediaStore.Images.ImageColumns.DATA };

                    String largeFileSort = MediaStore.Images.ImageColumns._ID
                            + " DESC";
                    myCursor = getActivity().managedQuery(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            largeFileProjection, null, null, largeFileSort);
                    String largeImagePath = "";

                    try {
                        myCursor.moveToFirst();

                        // This will actually give yo uthe file path location of the
                        // image.
                        largeImagePath = myCursor
                                .getString(myCursor
                                        .getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATA));
                        selectedImageUri = Uri.fromFile(new File(
                                largeImagePath));

                    } finally {
                        // myCursor.close();
                    }
                    // These are the two URI's you'll be interested in. They give
                    // you a
                    // handle to the actual images
                    Uri uriLargeImage = Uri.withAppendedPath(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            String.valueOf(imageId));
                    Uri uriThumbnailImage = Uri.withAppendedPath(
                            MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
                            String.valueOf(thumbnailImageId));

                    // I've left out the remaining code, as all I do is assign the
                    // URI's
                    // to my own objects anyways...
                } catch (Exception e) {

                    Log.i("TAG",
                            "inside catch Samsung Phones exception " + e.toString());

                }


                try {

                    Log.i("TAG", "URI Normal:" + selectedImageUri.getPath());
                } catch (Exception e) {
                    Log.i("TAG", "Excfeption inside Normal URI :" + e.toString());
                }
                pickphoto.setVisibility(View.GONE);
                setphoto.setVisibility(View.VISIBLE);
                selectedImage.setAdjustViewBounds(true);
                Path = ImageHelper2.compressImage(selectedImageUri, getActivity());
                bitmap = ImageHelper2.decodeSampledBitmapFromFile(Path,screenWidth,screenHeight);
                selectedImage.setImageBitmap(bitmap);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] b = baos.toByteArray();

                String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                editor.putString(ChooPref.TICKET_IMAGE, encodedImage);
                editor.putString(ChooPref.SELECTED_IMAGE_URI, selectedImageUri+"");
                editor.commit();


            }catch (Exception e){

            }
        }
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    public String getPath(Uri uri) {

        String[] projection = { MediaStore.Images.Media.DATA };

        Cursor cursor = getActivity().managedQuery(uri, projection, null, null, null);

        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        return cursor.getString(column_index);

    }

}
