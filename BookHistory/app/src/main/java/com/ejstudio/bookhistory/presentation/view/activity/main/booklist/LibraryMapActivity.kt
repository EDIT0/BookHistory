package com.ejstudio.bookhistory.presentation.view.activity.main.booklist

import android.Manifest
import android.content.Context
import android.graphics.PointF
import android.location.Address
import android.os.Build
import android.os.Bundle
import android.util.Log
import com.ejstudio.bookhistory.R
import com.ejstudio.bookhistory.databinding.ActivityLibraryMapBinding
import com.ejstudio.bookhistory.presentation.base.BaseActivity
import com.ejstudio.bookhistory.presentation.view.viewmodel.main.booklist.LibraryMapViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import android.view.View

import android.view.ViewGroup
import androidx.annotation.UiThread
import androidx.fragment.app.FragmentManager
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.widget.LocationButtonView
import com.naver.maps.map.widget.ZoomControlView
import com.naver.maps.map.util.FusedLocationSource
import android.location.Geocoder
import com.ejstudio.bookhistory.util.DistrictCode
import java.io.IOException
import java.util.*
import android.location.LocationManager

import androidx.core.app.ActivityCompat

import android.content.pm.PackageManager
import android.location.Location
import com.naver.maps.map.widget.CompassView
import java.lang.Exception


class LibraryMapActivity : BaseActivity<ActivityLibraryMapBinding>(R.layout.activity_library_map),
    OnMapReadyCallback {

    private val LOCATION_PERMISSION_REQUEST_CODE = 1000
    private var locationManager: LocationManager? = null
    private val REQUEST_CODE_LOCATION = 2
    var initMyLocationLat: Double = 0.0
    var initMyLocationLong: Double = 0.0

    private val TAG: String? = LibraryMapActivity::class.java.simpleName
    public val libraryMapViewModel: LibraryMapViewModel by viewModel()

    lateinit var naverMap: NaverMap
    lateinit var uiSettings: UiSettings
    lateinit var mapFragment: MapFragment
    private var locationSource: FusedLocationSource? = null

//    var geocoder = Geocoder(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.libraryMapViewModel = libraryMapViewModel

        val fm = supportFragmentManager
        mapFragment = fm.findFragmentById(R.id.map) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map, it).commit()
            }

        mapFragment.getMapAsync(this)

        checkPermissions()

        locationManager = binding.root.context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
//사용자의 현재 위치
        var userLocation = getMyLocation()
        if( userLocation != null ) {
            initMyLocationLat = userLocation.getLatitude()
            initMyLocationLong = userLocation.getLongitude()
//            userVO.setLat(latitude);
//            userVO.setLon(longitude);
            Log.i(TAG, "현재 내 위치값 : "+ initMyLocationLat+","+initMyLocationLong)
        }





    }
    fun recvIntent() {
        libraryMapViewModel.bookISBN = intent.extras?.getString("book_isbn")?:""
        Log.i(TAG, "책 isbn: " + libraryMapViewModel.bookISBN)

        Log.i(TAG, "isbn 코드: ${libraryMapViewModel.bookISBN.length}")
    }

    fun viewModelCallback() {
        with(libraryMapViewModel) {

        }
    }

    var permissionlistener: PermissionListener = object : PermissionListener {
        override fun onPermissionGranted() { // 권한 허가시 실행 할 내용
            initView()
        }
        override fun onPermissionDenied(deniedPermissions: ArrayList<String>?) {
            // 권한 거부시 실행  할 내용
            showToast("권한 허용을 하지 않으면 서비스를 이용할 수 없습니다.")
            onBackPressed()
        }
    }

    private fun checkPermissions() {
        if (Build.VERSION.SDK_INT >= 23) { // 마시멜로(안드로이드 6.0) 이상 권한 체크
            TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setRationaleMessage("앱을 이용하기 위해서는 접근 권한이 필요합니다")
                .setDeniedMessage("앱에서 요구하는 권한설정이 필요합니다.\n [설정] > [권한] 에서 사용으로 활성화해주세요.")
                .setPermissions(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ).check()
        } else {
            initView()
        }
    }

    fun initView() {
        recvIntent()
        viewModelCallback()
    }

    @UiThread
    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
        uiSettings = naverMap.uiSettings

        naverMapSetting()
        naverMapClickListener()
        naverMapEventListener()

    }

    fun naverMapSetting() {

        // 처음 내 위치
        val cameraUpdate = CameraUpdate.scrollTo(LatLng(initMyLocationLat, initMyLocationLong))
        naverMap.moveCamera(cameraUpdate)

        uiSettings.isZoomControlEnabled = false
        uiSettings.isCompassEnabled = false
//        uiSettings.isScaleBarEnabled = false
//        uiSettings.isLocationButtonEnabled = false

        mapFragment.getMapAsync {
            val currentLocationButtonView = findViewById(R.id.currentLocationButtonView) as LocationButtonView
            currentLocationButtonView.map = naverMap
            locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
            naverMap.setLocationSource(locationSource)
            naverMap.setLocationTrackingMode(LocationTrackingMode.None)

        }
    }

    fun naverMapClickListener() {
        naverMap.setOnMapClickListener(object : NaverMap.OnMapClickListener{
            override fun onMapClick(p0: PointF, p1: LatLng) {
                Log.i(TAG, "이건뭐지? ${p0} / ${p1}")
                Log.i(TAG, getAddress(p1.latitude, p1.longitude))
                for(i in DistrictCode.districtCodeMap) {
                    Log.i(TAG, "돌아간다~")
                    if(i.key.equals("b")) {
                        Log.i(TAG, "${i.value} / ${i.key}")
                        return
                    }
                }
            }

        })
    }

    fun naverMapEventListener() {

        // 카메라 이동 후 위치
        naverMap.addOnCameraIdleListener {
            Log.i(TAG, "제발나와라~ ${getAddress(naverMap.cameraPosition.target.latitude, naverMap.cameraPosition.target.longitude)}")
        }

        // 현 위치 버튼 누른 후 좌표 리스너
//        naverMap.addOnLocationChangeListener { location ->
//            Log.i(TAG, "리스너: ${getAddress(location.latitude, location.longitude)}")
//        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (locationSource!!.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    // 좌표 -> 주소 변환
    private fun getAddress(lat: Double, lng: Double): String {
        val geoCoder = Geocoder(binding.root.context, Locale.KOREA)
        val address: ArrayList<Address>
        var addressResult = "주소를 가져 올 수 없습니다."
        try {
            //세번째 파라미터는 좌표에 대해 주소를 리턴 받는 갯수로
            //한좌표에 대해 두개이상의 이름이 존재할수있기에 주소배열을 리턴받기 위해 최대갯수 설정
            address = geoCoder.getFromLocation(lat, lng, 1) as ArrayList<Address>
            if (address.size > 0) {
                // 주소 받아오기
                val currentLocationAddress = address[0].getAddressLine(0).toString()

                //split 분리된 문자를 담을 배열 선언 실시
                var str_arr = currentLocationAddress.split(" ")

                try {
                    var str1 = str_arr.get(2).get(str_arr.get(2).length - 1)
                    Log.i(TAG, "너 뭐나오니1 ${str1}")
                    if ((str1.toString()).equals("시")) {
                        addressResult = str_arr.get(1) + " " + str_arr.get(2)
                        var str2 = str_arr.get(3).get(str_arr.get(3).length - 1)
                        Log.i(TAG, "너 뭐나오니2 " + str2)
                        if ((str2.toString()).equals("구")) {
                            addressResult =
                                str_arr.get(1) + " " + str_arr.get(2) + " " + str_arr.get(3)

                        }
                    } else {
                        addressResult = str_arr.get(1) + " " + str_arr.get(2)
                    }
                }catch (e : Exception) {
                    addressResult = "주소를 가져 올 수 없습니다."
                }
            }

            // 경기도 부천시 원미구 -> 없으면 -> 경기도 부천시 로 검색
            // 경기도 수원시 팔달구 -> 있으면 -> 끝

        } catch (e: IOException) {
            e.printStackTrace()
        }
        return addressResult
    }


    private fun getMyLocation(): Location? {
        var currentLocation: Location? = null
        // Register the listener with the Location Manager to receive location updates
        if (ActivityCompat.checkSelfPermission(binding.root.context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(binding.root.context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            println("////////////사용자에게 권한을 요청해야함")
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE_LOCATION
            )
            getMyLocation() //이건 써도되고 안써도 되지만, 전 권한 승인하면 즉시 위치값 받아오려고 썼습니다!
        } else {
            println("////////////권한요청 안해도됨")

            // 수동으로 위치 구하기
            val locationProvider = LocationManager.GPS_PROVIDER
            currentLocation = locationManager!!.getLastKnownLocation(locationProvider)
            if (currentLocation != null) {
                val lng = currentLocation.longitude
                val lat = currentLocation.latitude
            }
        }
        return currentLocation
    }


}