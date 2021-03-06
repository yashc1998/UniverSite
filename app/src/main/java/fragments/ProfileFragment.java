package fragments;


import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import adapters.InNewsAdapter;
import controllers.PostsAsyncResponce;
import controllers.UserAsyncResponce;
import data_provider.InNewsPosts;
import data_provider.UserData;
import modals.PostModal;
import modals.UserModal;
import universite.com.parasite.R;
import universite.com.parasite.SharedPrefManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private CollapsingToolbarLayout toolbarLayout;
    private RecyclerView personal_posts_recyclerview;
    private AppBarLayout appBarLayout;
    private ImageView profileImage;
    private ProgressBar progressBar;
    private TextView collegeName, courseName;
    private UserData userData;
    private int nameTextColor = Color.WHITE;
    private Bitmap profileImageBitmap;
    private Toolbar profileToolbar;

    public ProfileFragment() {
        // Required empty public constructor
        userData = new UserData(getActivity());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_profile, container, false);
        userData.getUserData(SharedPrefManager.getInstance(getActivity()).getUser().getmID(), new UserAsyncResponce() {
            @Override
            public void userdata(UserModal modal) {
                Log.v(ProfileFragment.class.getSimpleName(), modal.getmProfilePic());
                collegeName.setText(SharedPrefManager.getInstance(getActivity()).getUser().getmCollege());
                courseName.setText(SharedPrefManager.getInstance(getActivity()).getUser().getmCourse());
                Picasso.get()
                        .load(modal.getmProfilePic())
                        .resize(0, 300)
                        .placeholder(R.drawable.ic_person_white_24dp)
                        .into(profileImage);
            }
        });

        profileToolbar = view.findViewById(R.id.profile_toolbar);
        int count = 0;
        appBarLayout = view.findViewById(R.id.appbar_layout);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset)-appBarLayout.getTotalScrollRange() == 0)
                {

//                    view.findViewById(R.id.profile_info_layout).animate().alpha(0).scaleY(0);
//                    personal_posts_recyclerview.animate().translationYBy(-200);
                }
                else
                {
//                    view.findViewById(R.id.profile_info_layout).animate().alpha(1).scaleY(1);
//                    personal_posts_recyclerview.animate().translationYBy(200);


                }
            }
        });

        toolbarLayout = view.findViewById(R.id.collapsingToolbar_profile);
        toolbarLayout.setTitle(SharedPrefManager.getInstance(getActivity()).getUser().getmFullName());
        toolbarLayout.setExpandedTitleTextColor(ColorStateList.valueOf(nameTextColor));
        toolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
        

        progressBar = view.findViewById(R.id.profile_progress_bar);

        collegeName = view.findViewById(R.id.profile_college_name);
        courseName = view.findViewById(R.id.profile_course_name);

        collegeName.setText(SharedPrefManager.getInstance(getActivity()).getUser().getmCollege());
        courseName.setText(SharedPrefManager.getInstance(getActivity()).getUser().getmCourse());

        profileImage = view.findViewById(R.id.profile_image);
        Picasso.get()
                .load(SharedPrefManager.getInstance(getActivity()).getUser().getmProfilePic())
                .resize(0, 300)
                .placeholder(R.drawable.ic_person_white_24dp)
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .into(profileImage);



        personal_posts_recyclerview = view.findViewById(R.id.personal_posts_recyclerview);
        personal_posts_recyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        personal_posts_recyclerview.setHasFixedSize(true);
        new InNewsPosts(getContext()).fetchInNewsPosts(new PostsAsyncResponce() {
            @Override
            public void fetchSuccess(ArrayList<PostModal> posts) {

                ArrayList<PostModal> personal_post_list = new ArrayList<>();

                for (PostModal modal:
                     posts) {
                    if(modal.getmCreatorName().equalsIgnoreCase(SharedPrefManager.getInstance(getActivity()).getUser().getmUsername())){
                        personal_post_list.add(modal);
                    }
                }

                InNewsAdapter newsAdapter = new InNewsAdapter(personal_post_list, getActivity());
                progressBar.setVisibility(View.GONE);
                personal_posts_recyclerview.setAdapter(newsAdapter);
            }
        });



        return view;
    }


}
