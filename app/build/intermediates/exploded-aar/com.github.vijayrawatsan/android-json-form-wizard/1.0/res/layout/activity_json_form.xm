<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
                xmlns:app="http://schemas.android.com/apk/res-auto"
                android:layout_width="match_parent"
                android:layout_height="match_parent">

    <android.support.v7.widget.Toolbar
        android:id="@+id/tb_top"
        android:layout_alignParentStart="true"
        android:layout_alignParentLeft="true"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_alignParentTop="true"
        android:background="?attr/colorPrimary"
        android:minHeight="?attr/actionBarSize"
        app:theme="@style/AppTheme.DarkToolbar"/>

    <FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
                 android:id="@+id/container"
                 android:layout_below="@+id/tb_top"
                 android:layout_width="match_parent"
                 android:layout_height="match_parent"
        />
</RelativeLayout>
<!-- From: file:/build/library/src/main/res/layout/activity_json_form.xml -->