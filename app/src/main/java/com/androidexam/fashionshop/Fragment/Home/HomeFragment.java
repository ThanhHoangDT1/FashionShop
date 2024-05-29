package com.androidexam.fashionshop.Fragment.Home;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.androidexam.fashionshop.Adapter.ProductAdapter;
import com.androidexam.fashionshop.Api.ApiService;
import com.androidexam.fashionshop.Decoration.ColumnSpacingItemDecoration;
import com.androidexam.fashionshop.MainActivity;
import com.androidexam.fashionshop.Model.Category;
import com.androidexam.fashionshop.Model.Product;
import com.androidexam.fashionshop.Model.ProductResponse;

import com.androidexam.fashionshop.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private boolean isSearching = false;
    private boolean isFiltering = false;
    private ProductAdapter adapter;
    private List<Product> productList;
    private EditText edtSearch;
    private ImageButton btnSearch;
    private ImageButton btnFilter;

    private  RecyclerView recyclerView;
    String searchQuery;
    private List<Product> originalProductList; // Danh sách sản phẩm gốc


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        productList = new ArrayList<>();
        originalProductList = new ArrayList<>();
    }
    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        showBottomNavigationView();
        iconHome();
        recyclerView = view.findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), 2);
        int space = 50;
        recyclerView.addItemDecoration(new ColumnSpacingItemDecoration(space));
        btnSearch = view.findViewById(R.id.btn_search);
        btnFilter = view.findViewById(R.id.btn_filter);
        edtSearch = view.findViewById(R.id.edt_search);
        productList = new ArrayList<>();
        adapter = new ProductAdapter(productList, requireActivity().getSupportFragmentManager());
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSearch();
            }
        });
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performFilter();
            }
        });

        recyclerView.setAdapter(adapter);
        searchQuery = edtSearch.getText().toString().trim();
        // Fetch product data from API and update the RecyclerView
        updateRecyclerView();

        return view;
    }
    private void fetchProductData() {
       // ApiService.productService.getAllProducts(1).enqueue(new Callback<ProductResponse>() {
        ApiService.productService.getProductsall().enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                if (response.isSuccessful()) {
                    List<Product> newProducts = response.body().getItems();
                    Log.d("THU HAHA", "onResponse: "+newProducts.get(1).getProductImage().toString());
                    if (newProducts != null && !newProducts.isEmpty()) {
                        originalProductList.clear();
                        originalProductList.addAll(newProducts);
                        productList.clear();
                        productList.addAll(newProducts);
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    Log.e("API Error", "Response is not successful");
                }

            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                String errorMessage = "API Call Failed: " + t.getMessage();
                Log.e("API Error", errorMessage);
            }
        });
    }

    private void performFilter() {

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_filter, null);
        builder.setView(dialogView);

        Spinner categorySpinner = dialogView.findViewById(R.id.category);
        List<Category> categoryList = new ArrayList<>();


        fetchCategoriesFromApi(categorySpinner, categoryList);

        EditText edtmin = dialogView.findViewById(R.id.edtmin);
        EditText edtmax = dialogView.findViewById(R.id.edtmax);

        edtmin.setInputType(InputType.TYPE_CLASS_NUMBER);
        edtmax.setInputType(InputType.TYPE_CLASS_NUMBER);

        RadioGroup radioGroup = dialogView.findViewById(R.id.radioGroupPrice);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.under100:
                        edtmin.setText("0");
                        edtmax.setText("400000");
                        break;
                    case R.id.between100and200:
                        edtmin.setText("400000");
                        edtmax.setText("800000");
                        break;
                    case R.id.between200and300:
                        edtmin.setText("800000");
                        edtmax.setText("1000000");
                        break;
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        Button btnReset = dialogView.findViewById(R.id.btnreset);

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categorySpinner.setSelection(0);
                radioGroup.clearCheck();
                edtmin.setText("");
                edtmax.setText("");
            }
        });

        dialog.findViewById(R.id.btnapply).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedCategory = ((Category) categorySpinner.getSelectedItem()).getName();
                String minPrice = edtmin.getText().toString().trim();
                 String maxPrice =edtmax.getText().toString().trim();
                Log.d("API_CALL", "Selected Category: " + selectedCategory);
                Log.d("API_CALL", "Min Price: " + minPrice);
                Log.d("API_CALL", "Max Price: " + maxPrice);
                if (selectedCategory.isEmpty()) {
                    handleFilterWithoutCategory(minPrice, maxPrice);
                } else {
                    handleFilterWithCategory(selectedCategory, minPrice, maxPrice);
                }

                dialog.dismiss();
            }
        });
    }


    private void handleFilterWithoutCategory(String minPrice, String maxPrice) {
        ApiService.productService.getProducts(searchQuery, minPrice, maxPrice, null)
                .enqueue(new Callback<ProductResponse>() {
                    @Override
                    public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {

                        if (response.isSuccessful()) {
                            List<Product> filteredProducts = response.body().getItems();
                            if (filteredProducts != null && !filteredProducts.isEmpty()) {
                                productList.clear();
                                productList.addAll(filteredProducts);
                                adapter.notifyDataSetChanged();
                                originalProductList.clear();
                                originalProductList.addAll(filteredProducts);
                                isFiltering = true;
                                adapter.notifyDataSetChanged();

                            } else {
                                productList.clear();
                                adapter.notifyDataSetChanged();
                                Toast.makeText(requireContext(), "Không có kết quả", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(requireContext(), "Lỗi khi gọi API", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<ProductResponse> call, Throwable t) {
                        Toast.makeText(requireContext(), "Lỗi khi gọi API", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void handleFilterWithCategory(String selectedCategory, String minPrice, String maxPrice) {
        ApiService.productService.getProducts(null, minPrice, maxPrice, selectedCategory)
                .enqueue(new Callback<ProductResponse>() {
                    @Override
                    public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                        if (response.isSuccessful()) {
                            List<Product> filteredProducts = response.body().getItems();
                            if (filteredProducts != null && !filteredProducts.isEmpty()) {
                                productList.clear();
                                productList.addAll(filteredProducts);
                                adapter.notifyDataSetChanged();
                                originalProductList.clear();
                                originalProductList.addAll(filteredProducts);
                                isFiltering = true;
                                adapter.notifyDataSetChanged();
                            } else {
                                productList.clear();
                                adapter.notifyDataSetChanged();
                                Toast.makeText(requireContext(), "Không có kết quả", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(requireContext(), "Lỗi khi gọi API", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<ProductResponse> call, Throwable t) {
                        Toast.makeText(requireContext(), "Lỗi khi gọi API", Toast.LENGTH_SHORT).show();
                    }
                });

    }
    private void performSearch() {
        searchQuery = edtSearch.getText().toString().trim();

        if (!searchQuery.isEmpty()) {
            searchProducts(searchQuery);
            hideKeyboard();
        } else {
            searchProducts(null);
            //Toast.makeText(requireContext(), "Nhập từ khóa để tìm kiếm", Toast.LENGTH_SHORT).show();
        }
    }

    private void searchProducts(String keyword) {
        ApiService.productService.getProducts(keyword).enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                if (response.isSuccessful()) {
                    List<Product> searchResults = response.body().getItems();
                    if (searchResults != null) {
                        if (!searchResults.isEmpty()) {
                            productList.clear();
                            productList.addAll(searchResults);
                            originalProductList.clear();
                            originalProductList.addAll(searchResults);
                            isSearching = true;
                            adapter.notifyDataSetChanged();
                        } else {
                            productList.clear();
                            adapter.notifyDataSetChanged();
                            Toast.makeText(requireContext(), "Không có kết quả tìm kiếm", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Log.e("API Error", "Search API response is not successful");
                }

            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                String errorMessage = "Search API Call Failed: " + t.getMessage();
                Log.e("API Error", errorMessage);
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        View focusedView = requireActivity().getCurrentFocus();
        if (focusedView != null) {
            imm.hideSoftInputFromWindow(focusedView.getWindowToken(), 0);
        }
    }

    public void updateRecyclerView() {
        Log.d("UpdateRecyclerView", "Original Product List: " + originalProductList);

        if(isSearching & isFiltering){
            productList.clear();
            productList.addAll(originalProductList);
            adapter.notifyDataSetChanged();
            isSearching = false;
            isFiltering = false;
        } else if (isSearching) {
            productList.clear();
            productList.addAll(originalProductList);
            adapter.notifyDataSetChanged();
            isSearching = false;
        } else if (isFiltering) {productList.clear();
            productList.addAll(originalProductList);
            adapter.notifyDataSetChanged();
            isFiltering = false;

        } else {
            fetchProductData();
        }
    }
    private  void iconHome() {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).onCartFragmentBackPressed();
        }
    }

    private  void showBottomNavigationView() {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).showBottomNavigation();
        }
    }
    private void fetchCategoriesFromApi(Spinner categorySpinner, List<Category> categoryList) {
        ApiService.productService.getCategory().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful()) {
                    List<Category> apiCategories = response.body();
                    if (apiCategories != null && !apiCategories.isEmpty()) {
                        Category emptyCategory = new Category();
                        emptyCategory.setName("");
                        categoryList.add(emptyCategory);

                        categoryList.addAll(apiCategories);

                        ArrayAdapter<Category> categoryAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, categoryList);
                        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        categorySpinner.setAdapter(categoryAdapter);
                    }
                } else {
                    Log.e("API Error", "Failed to fetch categories: " + response.message());
                }

            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Log.e("API Error", "API Call Failed: " + t.getMessage());
            }
        });
    }


}
