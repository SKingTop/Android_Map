package kz.sking.map;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import static kz.sking.map.R.drawable;
import static kz.sking.map.R.id;
import static kz.sking.map.R.layout;
import static kz.sking.map.R.string;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap; // Карта
    private TextView textView; // Текстовый компонет

    private String markerTitle = ""; // Название выбранного маркера
    private String markerFileName = ""; // Имя файла с подробными данными выбранного маркера

    private final int SITYSCALE = 15; // Масштаб для отображения карты

    static final LatLng startMarker = new LatLng(39.668362, -103.215478); // Начальный маркер
    static final LatLng marker1 = new LatLng(37.331822, -122.030177); // Маркер Apple
    static final LatLng marker2 = new LatLng(37.485098, -122.147425); // Маркер Facebook
    static final LatLng marker3 = new LatLng(37.422042, -122.084063); // Маркер Google
    static final LatLng marker4 = new LatLng(47.642462, -122.136884); // Маркер Microsoft

    private static final float ALPHA = 0.8f; // Коэффициент прозрачности для маркеров

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_maps);

        // Доступ к карте
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(id.map);
        mapFragment.getMapAsync(this);

        textView = (TextView) findViewById(id.textViewInfo); // Доступ к компоненту "textViewInfo"
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Обычный тип карты
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // Добавление маркера на карту с текстом
        mMap.addMarker(new MarkerOptions().position(startMarker).title((getString(R.string.startMarker_title))));

        // Добавление маркера на карту с текстом, иконкой и полупрозрачностью
        mMap.addMarker(new MarkerOptions().position(marker1).title(getString(R.string.marker1_title)).icon(
                BitmapDescriptorFactory.fromResource(drawable.apple)).alpha(ALPHA).snippet(getString(R.string.marker1_txt_click)));

        // Добавление маркера на карту с текстом, иконкой и полупрозрачностью
        mMap.addMarker(new MarkerOptions().position(marker2).title((getString(R.string.marker2_title))).icon(
                BitmapDescriptorFactory.fromResource(drawable.facebook)).alpha(ALPHA).
                snippet(getString(R.string.marker2_txt_click)));
        // Добавление маркера на карту с текстом, иконкой и полупрозрачностью
        mMap.addMarker(new MarkerOptions().position(marker3).title((getString(R.string.marker3_title))).icon(
                BitmapDescriptorFactory.fromResource(drawable.google)).alpha(ALPHA).
                snippet(getString(R.string.marker3_txt_click)));
        // Добавление маркера на карту с текстом, иконкой и полупрозрачностью
        mMap.addMarker(new MarkerOptions().position(marker4).title((getString(R.string.marker4_title))).icon(
                BitmapDescriptorFactory.fromResource(drawable.microsoft)).alpha(ALPHA).
                snippet(getString(R.string.marker4_txt_click)));

        //Разрешение изменения масштаба карты
        mMap.getUiSettings().setZoomControlsEnabled(true);

        // Проверка на включенный GPS и позиционирование на карте
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //Показать текущее местоположение по GPS
            mMap.setMyLocationEnabled(true);
        }

        // Переход просмотра на карте на нужный маркер c зумом
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startMarker, SITYSCALE));

        // Инициализация стартового маркера
        onMarkerClick(getString(string.startMarker_id));

        // Обработчик нажатия на маркеры карты
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.showInfoWindow();
                MapsActivity.this.onMarkerClick(marker.getId());
                return true;
            }
        });
    }

    // Нажатие на маркер
    public void onMarkerClick(String idMarker) {
        switch (idMarker) {
            case "m0":
                doClickMarker(startMarker, getString(R.string.startMarker_info), getString(string.startMarker_title), getString(string.startMarker_file));
                break;
            case "m1":
                doClickMarker(marker1, getString(R.string.marker1_info), getString(string.marker1_title), getString(string.marker1_file));
                break;
            case "m2":
                doClickMarker(marker2, getString(R.string.marker2_info), getString(string.marker2_title), getString(string.marker2_file));
                break;
            case "m3":
                doClickMarker(marker3, getString(R.string.marker3_info), getString(string.marker3_title), getString(string.marker3_file));
                break;
            case "m4":
                doClickMarker(marker4, getString(R.string.marker4_info), getString(string.marker4_title), getString(string.marker4_file));
                break;
        }
    }

    // Обработка нажатия на маркер
    public void doClickMarker(LatLng marker, String info, String markerTitle, String markerFileName) {
        this.markerTitle = markerTitle;
        this.markerFileName = markerFileName;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker, SITYSCALE));
        findViewById(id.sv1).scrollTo(0, 0);
        if (Build.VERSION.SDK_INT >= 24) {
            textView.setText(Html.fromHtml(info, Html.FROM_HTML_MODE_LEGACY));
        } else {
            textView.setText(Html.fromHtml(info));
        }
    }

    // Нажатие на кнопку маркера
    public void onClickButtonMarker(View view) {
        String idMarker = view.getTag().toString();
        onMarkerClick(idMarker);
    }

    // Обработчик кнопки "Подробно"
    public void detailButtonClick(View view) {
        if (!markerFileName.equals("")) {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra(getString(string.tMarker), markerTitle);
            intent.putExtra(getString(string.mfile), markerFileName);
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), string.selectOb, Toast.LENGTH_SHORT).show();
        }
    }

}
