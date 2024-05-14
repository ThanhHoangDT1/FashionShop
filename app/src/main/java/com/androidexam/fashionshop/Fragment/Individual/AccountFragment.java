package com.androidexam.fashionshop.Fragment.Individual;

import android.Manifest;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.androidexam.fashionshop.Api.ApiService;
import com.androidexam.fashionshop.Fragment.Pay.WebViewFragment;
import com.androidexam.fashionshop.MainActivity;
import com.androidexam.fashionshop.Model.User;
import com.androidexam.fashionshop.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountFragment extends Fragment {
    private LinearLayout changeAvatar;

    private TextView tvUser, tvName, tvEmail, tvBirthday, tvPhone, tvGender;
    private ImageView yourAvatar;
    private ImageButton btnEdit, back;
    private LinearLayout changePassword,waitConfirmLayout, waitDeliveryLayout, ratingLayout;
    private User user;
    private int userId;
    private int selectedYear = 2000;
    private int selectedMonth = 0;
    private int selectedDay = 1;
    private boolean isDataChanged = false;

    private String initialName,initialGender;
    private String initialBirthday, newBirthday;
    private String initialEmail;
    private String initialPhoneNumber;
    private String newImagePath;
    private Uri newUri;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int YOUR_REQUEST_CODE = 123;
    private String newGender;


    public AccountFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.account_user, container, false);

        hideBottomNavigationView();
        SharedPreferences preferences = requireContext().getSharedPreferences("ID_USER", Context.MODE_PRIVATE);
        userId = preferences.getInt("user_id", -1);
        // Ánh xạ các thành phần trong layout
        tvUser = view.findViewById(R.id.username);
        tvName = view.findViewById(R.id.name);
        tvEmail = view.findViewById(R.id.email);
        tvBirthday = view.findViewById(R.id.birthday);
        yourAvatar = view.findViewById(R.id.yourAvatar);
        tvGender = view.findViewById(R.id.gender);
        tvPhone = view.findViewById(R.id.phonnumber);
        btnEdit = view.findViewById(R.id.edit);
        back = view.findViewById(R.id.back_button);
        changePassword = view.findViewById(R.id.lnchangePW);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        tvGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowEditGender();
            }
        });
        tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditNameDialog();

            }
        });
        tvPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditPhoneNumberDialog();

            }
        });
        tvBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        tvEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditEmailDialog();

            }
        });
        changeAvatar = view.findViewById(R.id.lnchangeavatar);
        changeAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();

                //openFileChooser();
            }
        });
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openChangePassword();
            }
        });

        String[] birthdayParts = tvBirthday.getText().toString().split("/");
        if (birthdayParts.length == 3) {
            selectedDay = Integer.parseInt(birthdayParts[0]);
            selectedMonth = Integer.parseInt(birthdayParts[1]) - 1;
            selectedYear = Integer.parseInt(birthdayParts[2]);
        }

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isDataChanged) {
//                    if (user.getUrlImage() != null && newImagePath != null) {
//
//                        if (!newImagePath.equals(user.getUrlImage())) {
//                            editAvartar(newUri);
//                            btnEdit.setVisibility(View.INVISIBLE);
//                        } else {
//
//                            isDataChanged = false;
//
//                            User editedUser = new User();
//                            if (!tvName.getText().toString().equals(initialName)) {
//                                editedUser.setName(tvName.getText().toString());
//                            }
//
//                            if (tvGender.getText().toString() == "" && initialGender == null) {
//                                // Cả hai đều là null, được xem là giống nhau
//                            } else if (tvGender.getText() != null && tvGender.getText().toString().equals(initialGender)) {
//                                // Giá trị của tvPhone giống với initialPhoneNumber
//                            } else {
//                                editedUser.setGender(tvGender.getText().toString());
//                            }
//
//                            if (!tvBirthday.getText().toString().equals(initialBirthday)) {
//                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
//                                String dateString= tvBirthday.getText().toString();
//
//                                LocalDate localDate = LocalDate.parse(dateString, formatter);
//
//                                editedUser.setDateOfBirth(dateString);
//                            }
//
//
//
//
//
//                            if (tvPhone.getText().toString() == "" && initialPhoneNumber == null) {
//                                // Cả hai đều là null, được xem là giống nhau
//                            } else if (tvPhone.getText() != null && tvPhone.getText().toString().equals(initialPhoneNumber)) {
//                                // Giá trị của tvPhone giống với initialPhoneNumber
//                            } else {
//                                editedUser.setPhoneNumber(tvPhone.getText().toString());
//                            }
//
//
//                            if (tvEmail.getText().toString() == "" && initialEmail == null) {
//                                // Cả hai đều là null, được xem là giống nhau
//                            } else if (tvEmail.getText() != null && tvEmail.getText().toString().equals(initialEmail)) {
//                                // Giá trị của tvPhone giống với initialPhoneNumber
//                            } else {
//                                editedUser.setEmail(tvEmail.getText().toString());
//                            }
//
//                            editUserOnServer(editedUser);
//
//                            isDataChanged = false;
//                        }
//                        btnEdit.setVisibility(View.INVISIBLE);
//                    } else if (user.getUrlImage() == null && newImagePath != null) {
//                        editAvartar(newUri);
//                    } else {

                        User editedUser = new User();
                        if (!tvName.getText().toString().equals(initialName)) {
                            editedUser.setName(tvName.getText().toString());
                        }
                        if (!tvBirthday.getText().toString().equals(initialBirthday)) {

                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                            String dateString= tvBirthday.getText().toString();
                            LocalDate localDate = LocalDate.parse(dateString, formatter);
                            editedUser.setDateOfBirth(dateString);
                        }
                        tvGender.getText();
                        if (tvGender.getText().toString() == "" && initialGender == null) {
                            // Cả hai đều là null, được xem là giống nhau
                        } else if (tvGender.getText() != null && tvGender.getText().toString().equals(initialGender)) {
                            // Giá trị của tvPhone giống với initialPhoneNumber
                        } else {
                            editedUser.setGender(tvGender.getText().toString());
                        }


                        if (tvPhone.getText().toString() == "" && initialPhoneNumber == null) {
                            // Cả hai đều là null, được xem là giống nhau
                        } else if (tvPhone.getText() != null && tvPhone.getText().toString().equals(initialPhoneNumber)) {
                            // Giá trị của tvPhone giống với initialPhoneNumber
                        } else {
                            editedUser.setPhoneNumber(tvPhone.getText().toString());
                        }

                        if (tvEmail.getText().toString() == "" && initialEmail == null) {
                            // Cả hai đều là null, được xem là giống nhau
                        } else if (tvEmail.getText() != null && tvEmail.getText().toString().equals(initialEmail)) {
                            // Giá trị của tvPhone giống với initialPhoneNumber
                        } else {
                            editedUser.setEmail(tvEmail.getText().toString());
                        }

                        editUserOnServer(editedUser);

                        isDataChanged = false;
                    }
                    btnEdit.setVisibility(View.INVISIBLE);

                }

            //}
        });


        getUserDetail(userId);

        return view;
    }

    private void openChangePassword() {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        ChangePasswordFragment changePasswordFragment = new ChangePasswordFragment();

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.hide(this);
        transaction.replace(R.id.fragment_container, changePasswordFragment);
        transaction.addToBackStack("account");
        transaction.commit();
    }

    private void ShowEditGender() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.sizedialog, null);
        builder.setView(dialogView).setTitle("Edit Gender")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        ListView listView = dialogView.findViewById(R.id.listViewSize);
        List<String> genderList = Arrays.asList("MALE", "FEMALE", "OTHER");

        ArrayAdapter<String> sizeAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, genderList);
        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedgender = genderList.get(position);

                newGender =selectedgender;
                tvGender.setText(newGender);

                isDataChanged = !newGender.equals(user.getGender());
                alertDialog.dismiss();
                if (isDataChanged) {
                    btnEdit.setVisibility(View.VISIBLE);
                } else {
                    btnEdit.setVisibility(View.INVISIBLE);
                }

            }
        };
        listView.setOnItemClickListener(itemClickListener);
        listView.setAdapter(sizeAdapter);

        alertDialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == YOUR_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                openGallery();
            } else {
                Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show();

            }
        }
    }


    private void getUserDetail(int userId) {


        ApiService.productServiceWithToken.getUser(userId).enqueue(new Callback<User>() {

            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User user = response.body();
                    if (user != null) {
                        setUser(user);
                    }
                } else {
                    Log.e("User", "Error: " + response.message());
                    Log.e("User", "Error Code: " + response.code());
                }

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("User", "API Call Failed: " + t.getMessage());
            }
        });
    }

    public void setUser(User user) {
        this.user = user;
        displayUserData();
    }

    private void onBackPressed() {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.popBackStack();
    }

    private void displayUserData() {

        if (user.getUrlImage() == null) {
            String name = user.getName();
            String firstLetter = name.substring(0, 1).toLowerCase();

            int imageResource = getContext().getResources().getIdentifier(
                    firstLetter,
                    "drawable",
                    getContext().getPackageName()

            );
            Log.d("AvatarImage", "Image Resource: " + imageResource);
            yourAvatar.setImageResource(imageResource);
        } else {
            Picasso.get().load(user.getUrlImage()).into(yourAvatar);
        }
        tvUser.setText(user.getUsername());
        tvName.setText(user.getName());
        tvEmail.setText(user.getEmail());
        initialName = user.getName();
        initialEmail = user.getEmail();

        if (user.getDateOfBirth() != null) {
            initialBirthday = user.getDateOfBirth().toString();
            tvBirthday.setText(user.getDateOfBirth().toString());
        } else initialBirthday = "";
        if (user.getGender() != null) {
            tvGender.setText(user.getGender());
            initialGender = user.getGender();
        }
        if (user.getPhoneNumber() != null) {
            tvPhone.setText(user.getPhoneNumber());
            initialPhoneNumber = user.getPhoneNumber();

        }
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                selectedYear = year;
                selectedMonth = monthOfYear;
                String selectedDate;
                selectedDay = dayOfMonth;
                String day,month;
                if(selectedDay<10){
                    day ="0"+selectedDay;}
                else {
                    day =String.valueOf(selectedDay);
                }
                if(selectedMonth<10){
                     month ="0"+(selectedMonth+1);}
                else {
                    month =String.valueOf(selectedMonth+1);
                }

                selectedDate = selectedYear + "-" + month + "-" +day ;
                tvBirthday.setText(selectedDate);
                newBirthday = selectedDate;
                isDataChanged = !newBirthday.equals(initialBirthday);

                if (isDataChanged) {
                    btnEdit.setVisibility(View.VISIBLE);
                } else {
                    btnEdit.setVisibility(View.INVISIBLE);
                }
            }
        }, selectedYear, selectedMonth, selectedDay);

        datePickerDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            newUri = data.getData();


            newImagePath = getRealPathFromUri(newUri);


            Picasso.get().load(newUri).into(yourAvatar);


            isDataChanged = !newImagePath.equals(user.getUrlImage());

            Log.d("URL AVATAR", newUri.toString());
            if (isDataChanged) {
                editAvartar(newUri);
                //btnEdit.setVisibility(View.VISIBLE);
            } else {
                btnEdit.setVisibility(View.INVISIBLE);
            }
        }
    }

    private String getRealPathFromUri(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = requireActivity().getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) {
            return uri.getPath();
        } else {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String filePath = cursor.getString(columnIndex);
            cursor.close();
            return filePath;
        }
    }

    private void showEditNameDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_name, null);
        EditText edtNewName = dialogView.findViewById(R.id.edtNewName);


        edtNewName.setText(tvName.getText().toString());


        builder.setView(dialogView)
                .setTitle("Edit Name")
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Lưu tên mới và cập nhật giao diện
                        String newName = edtNewName.getText().toString();
                        tvName.setText(newName);


                        isDataChanged = !newName.equals(user.getName());

                        if (isDataChanged) {
                            btnEdit.setVisibility(View.VISIBLE);
                        } else {
                            btnEdit.setVisibility(View.INVISIBLE);
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = builder.create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {

                InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(edtNewName, InputMethodManager.SHOW_IMPLICIT);


                edtNewName.requestFocus();
                edtNewName.setSelection(0, edtNewName.getText().length());
            }
        });
        alertDialog.show();
    }


    private void showEditPhoneNumberDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_phone, null);
        EditText edtPhoneNumber = dialogView.findViewById(R.id.edtNewPhoneNumber);

        edtPhoneNumber.setText(tvPhone.getText().toString());

        builder.setView(dialogView)
                .setTitle("Edit Phone Number")
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String newPhone = edtPhoneNumber.getText().toString();
                        tvPhone.setText(newPhone);

                        isDataChanged = !newPhone.equals(user.getPhoneNumber());

                        if (isDataChanged) {
                            btnEdit.setVisibility(View.VISIBLE);
                        } else {
                            btnEdit.setVisibility(View.INVISIBLE);
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = builder.create();


        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {

                InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(edtPhoneNumber, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        alertDialog.show();
    }

    private void showEditEmailDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_email, null);
        EditText edtEmail = dialogView.findViewById(R.id.edtNewEmail);

        edtEmail.setText(tvEmail.getText().toString());

        builder.setView(dialogView)
                .setTitle("Edit Email ")
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newEmail = edtEmail.getText().toString();
                        tvEmail.setText(newEmail);

                        isDataChanged = !newEmail.equals(user.getEmail());

                        if (isDataChanged) {
                            btnEdit.setVisibility(View.VISIBLE);
                        } else {
                            btnEdit.setVisibility(View.INVISIBLE);
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(edtEmail, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        alertDialog.show();

    }


    private void editUserOnServer(User editedUser) {

        ApiService.productServiceWithToken.editUser(userId, editedUser).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User user = response.body();
                    if (user != null) {
                        setUser(user);
                        Toast.makeText(getContext(), "Thay đổi thông tin thành công.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("User", "Error: " + response.message());
                    Log.e("User", "Error Code: " + response.code());
                    Toast.makeText(getContext(), "Thay đổi thông tin không thành công.", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("User", "API Call Failed: " + t.getMessage());
            }
        });
    }


    private void editAvartar(Uri url) {
        String realpath = RealPathUtil.getRealPath(getContext(), url);
        Log.e("CODE", realpath);
        SharedPreferences preferencesac = MyApp.getAppContext().getSharedPreferences("ACCESS", Context.MODE_PRIVATE);
        String accessToken = preferencesac.getString("accessToken", null);
        Log.e("avatar", "acc: " + accessToken);

        File file = new File(realpath);
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);

        //RequestBody requestAvatar = RequestBody.create(MediaType.parse("multipart/from-data"),file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("avatar", file.getName(), requestFile);

        ApiService.productServiceWithToken.uploadImage(userId, body).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                // Xử lý khi upload thành công
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Thay đổi ảnh thành công", Toast.LENGTH_SHORT).show();

                } else {
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("AVATAR", "API Call Failed: " + t.getMessage());
            }
        });
    }


    private void openGallery() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    YOUR_REQUEST_CODE);
        } else {

            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        }
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void hideBottomNavigationView() {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).hideBottomNavigation();
        }
    }



}
