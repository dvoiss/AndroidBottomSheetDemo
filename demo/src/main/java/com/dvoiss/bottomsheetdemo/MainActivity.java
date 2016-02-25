package com.dvoiss.bottomsheetdemo;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetBehavior.BottomSheetCallback;
import android.support.design.widget.BottomSheetBehavior.State;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.support.design.widget.BottomSheetBehavior.STATE_COLLAPSED;
import static android.support.design.widget.BottomSheetBehavior.STATE_EXPANDED;
import static android.support.design.widget.BottomSheetBehavior.STATE_DRAGGING;
import static android.support.design.widget.BottomSheetBehavior.STATE_HIDDEN;
import static android.support.design.widget.BottomSheetBehavior.STATE_SETTLING;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.root_view) View rootView;
    @Bind(R.id.bottom_sheet) View bottomSheet;
    @Bind(R.id.bottom_sheet_header) View bottomSheetHeader;
    @Bind(R.id.bottom_sheet_status) TextView bottomSheetStatus;

    private BottomSheetBehavior bottomSheetBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setBottomSheetCallback(new UpdateBottomSheetStatusTextCallback());

        // Attach view-tree observer to set the bottom sheet's peek-height once the view is laid out.
        attachViewTreeObserver();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.show_peek:
                setBottomSheetPeekHeight(true);
                return true;
            case R.id.no_peek:
                setBottomSheetPeekHeight(false);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * When the button is tapped open or close the button sheet.
     */
    @OnClick(R.id.toggle_bottom_sheet_button)
    public void toggleButtonSheet(View v) {
        bottomSheetBehavior.setState(getNewState());
    }

    /**
     * If the bottom sheet's header-text is clicked expand the sheet.
     */
    @OnClick(R.id.bottom_sheet_header)
    public void openButtonSheet(View v) {
        bottomSheetBehavior.setState(STATE_EXPANDED);
    }

    @State
    private int getNewState() {
        return bottomSheetBehavior.getState() == STATE_COLLAPSED ? STATE_EXPANDED : STATE_COLLAPSED;
    }

    /**
     * Once the view has been created get the height of the bottom-sheet's header-text and
     * use it to set the peek-height of the bottom-sheet.
     */
    private void attachViewTreeObserver() {
        ViewTreeObserver viewTreeObserver = rootView.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    rootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    setBottomSheetPeekHeight(true);
                }
            });
        }
    }

    private void setBottomSheetPeekHeight(boolean shouldShowPeek) {
        bottomSheetBehavior.setPeekHeight(shouldShowPeek ? bottomSheetHeader.getHeight() : 0);
        bottomSheetBehavior.setState(STATE_COLLAPSED);
    }

    class UpdateBottomSheetStatusTextCallback extends BottomSheetCallback {
        public void onStateChanged(@NonNull View bottomSheet, @State int newState) {
            bottomSheetStatus.setText(getStatusMessage(newState));
        }

        public void onSlide(@NonNull View bottomSheet, float slideOffset) {}

        private String getStatusMessage(int newState) {
            switch (newState) {
                case STATE_EXPANDED:
                    return getResources().getString(R.string.status_expanded);
                case STATE_COLLAPSED:
                    return getResources().getString(R.string.status_collapsed);
                case STATE_DRAGGING:
                    return getResources().getString(R.string.status_dragging);
                case STATE_SETTLING:
                    return getResources().getString(R.string.status_settling);
                case STATE_HIDDEN:
                    return getResources().getString(R.string.status_hidden);
                default:
                    return null;
            }
        }
    }
}
