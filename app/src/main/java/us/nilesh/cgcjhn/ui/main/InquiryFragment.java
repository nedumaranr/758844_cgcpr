package us.nilesh.cgcjhn.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import us.nilesh.cgcjhn.R;


public class InquiryFragment extends Fragment {

    View view;
//    private InquiryViewModel inquiryViewModel;
    Spinner spinner;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        inquiryViewModel = new ViewModelProvider(this).get(InquiryViewModel.class);

        view = inflater.inflate(R.layout.fragment_enquiry, container, false);

        spinner = view.findViewById(R.id.optionInquiry);
        String[] items = new String[]{"Not Selected","Accounts", "Admission", "Transport"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, items);
        spinner.setAdapter(adapter);

        return view;
    }
}