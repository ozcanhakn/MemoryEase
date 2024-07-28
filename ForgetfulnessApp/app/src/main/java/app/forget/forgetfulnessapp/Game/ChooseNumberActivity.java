package app.forget.forgetfulnessapp.Game;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;

import app.forget.forgetfulnessapp.R;

public class ChooseNumberActivity extends AppCompatActivity {
    private int selectedNumber = 1;
    private boolean checkBoxChecked = false;
    private boolean newBoardCreating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_choose_number);
        initializeSpinner();
        newBoardCreating = getIntent().getBooleanExtra("newBoard", false);
        if (newBoardCreating) {
            CheckBox checkBox = findViewById(R.id.checkBox);
            checkBox.setVisibility(View.INVISIBLE);
        }
    }

    private void initializeSpinner() {
        final Integer numbers[] = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        ArrayAdapter<Integer> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, numbers);
        Spinner spinner = findViewById(R.id.spinner);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedNumber = numbers[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void onCheckBoxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        checkBoxChecked = view.getId() == R.id.checkBox && checked;
    }

    public void chooseNumberButtonClicked(View view) {
        Intent intent = new Intent();
        intent.putExtra("chosenNumber", selectedNumber);
        intent.putExtra("isUnsure", checkBoxChecked);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void onRemovePieceButtonClicked(View view) {
        Intent intent = new Intent();
        intent.putExtra("removePiece", true);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void onGoBackButtonClicked(View view) {
        finish();
    }
}