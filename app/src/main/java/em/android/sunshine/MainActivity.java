package em.android.sunshine;

import android.app.ListActivity;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new SecondFragment())
                    .commit();
        }



    }

    public static class SecondFragment extends Fragment{

        ListView listView;

        public SecondFragment(){
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_first, container, false);
            List <String> data = Arrays.asList(
                    "Hoje - Doming - 29º ",
                    "Segunda - feira - 29º",
                    "Terça - feira - 30º",
                    "Quarta - feira - 31º",
                    "Quinta - feira - 28º",
                    "Sexta - feira - 27º",
                    "Sábado - 15º"

            );

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, data);
            listView  = (ListView) rootView.findViewById(R.id.list_view_forecast) ;
            listView.setAdapter(adapter);
            return rootView;
        }
    }
}
