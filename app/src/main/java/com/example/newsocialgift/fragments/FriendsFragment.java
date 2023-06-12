package com.example.newsocialgift.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.newsocialgift.R;
import com.example.newsocialgift.User;
import com.example.newsocialgift.adapters.FriendAdapter;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FriendsFragment extends Fragment {
    private RecyclerView recyclerView;
    private FriendAdapter friendAdapter;

    private static String URL = "";
    private static String TOKEN = null;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);
        Bundle arguments = getArguments();
        SharedPreferences preferences = requireActivity().getSharedPreferences("SocialGift", MODE_PRIVATE);
        TOKEN = preferences.getString("token", "");
        String userJson = preferences.getString("user", "");
        Gson gson = new Gson();
        User user = gson.fromJson(userJson, User.class);
        String mainID = user.getId();
        String userID = null;
        if (arguments != null) {
            userID = arguments.getString("userID");
        }
        URL = "https://balandrau.salle.url.edu/i3/socialgift/api/v1/users/";
        URL = URL.concat(userID).concat("/friends");
        recyclerView = view.findViewById(R.id.recyclerViewFriends);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<User> friendItemList = new ArrayList<>();
        friendAdapter = new FriendAdapter(friendItemList,userID,mainID, TOKEN);
        recyclerView.setAdapter(friendAdapter);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Utilities utilitiesFragment = new Utilities();
        fragmentTransaction.replace(R.id.fragment_container, utilitiesFragment);
        fragmentTransaction.commit();
        obtenerAmigos();

        return view;
    }

    private void obtenerAmigos() {
        RequestQueue mQueue = Volley.newRequestQueue(getContext());
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URL, null,
                response -> {
                    try {
                        List<User> friendItemList = new ArrayList<>();

                        for (int i = 0; i < response.length(); i++) {
                            JSONObject friendObject = response.getJSONObject(i);
                            String id = friendObject.getString("id");
                            String name = friendObject.getString("name");
                            String lastName = friendObject.getString("last_name");
                            String email = friendObject.getString("email");

                            String image = friendObject.getString("image");
                            User user = new User(id, name, lastName, email, image);
                            friendItemList.add(user);
                        }

                        friendAdapter.setFriendItemList(friendItemList);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                Throwable::printStackTrace
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("accept", "application/json");
                headers.put("Authorization", "Bearer " + TOKEN);
                return headers;
            }
        };

        mQueue.add(request);
    }
}
