package com.example.testspeedimport android.annotation.SuppressLintimport android.content.Contextimport android.net.ConnectivityManagerimport android.os.Bundleimport android.util.Logimport android.webkit.WebViewimport android.webkit.WebViewClientimport androidx.appcompat.app.AppCompatActivityimport android.widget.Buttonimport android.widget.Toastimport androidx.lifecycle.lifecycleScopeimport kotlinx.coroutines.Dispatchersimport kotlinx.coroutines.delayimport kotlinx.coroutines.launchimport okhttp3.ResponseBodyimport retrofit2.HttpExceptionimport retrofit2.Responseimport retrofit2.Retrofitimport retrofit2.converter.gson.GsonConverterFactoryclass MainActivity : AppCompatActivity() {    private lateinit var webView: WebView    @SuppressLint("SetJavaScriptEnabled")    override fun onCreate(savedInstanceState: Bundle?) {        super.onCreate(savedInstanceState)        setContentView(R.layout.activity_main)        // Инициализация WebView        webView = findViewById(R.id.webView)        // Настройка WebView        webView.settings.javaScriptEnabled = true        webView.settings.domStorageEnabled = true        webView.settings.allowContentAccess = true        webView.settings.setSupportMultipleWindows(true)        val retrofit = Retrofit.Builder()            .baseUrl("https://api-life3.megafon.tj")            .addConverterFactory(GsonConverterFactory.create())            .build()        val api = retrofit.create(SpeedTestService::class.java)        // Обработка нажатия кнопки        val btnMeasureSpeed: Button = findViewById(R.id.button)        btnMeasureSpeed.setOnClickListener {            val connectivityManager =                getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager            val networkInfo = connectivityManager.activeNetworkInfo            val isConnected = networkInfo != null && networkInfo.isConnected            //Проверка соединение            if (!isConnected) {                Toast.makeText(this, "Нет подключения к Интернету", Toast.LENGTH_SHORT).show()            }else {                // Загрузка сайта fast.com                webView.loadUrl("https://fast.com")                webView.webViewClient = object : WebViewClient() {                    override fun onPageFinished(view: WebView?, url: String?) {                    lifecycleScope.launch {                        delay(100)                        webView.evaluateJavascript("document.getElementById('speed-value').textContent") {                            val speedValue = it.trim()                            Log.d("fast", speedValue)                            webView.evaluateJavascript("document.getElementById('latency-value').textContent") {                                val latencyValue = it.trim()                                Log.d("fast", latencyValue)                                webView.evaluateJavascript("document.getElementById('bufferbloat-value').textContent") { it ->                                    val bufferBloatValue = it.trim()                                    Log.d("fast", bufferBloatValue)                                    webView.evaluateJavascript("document.getElementById('upload-value').textContent") {                                        val uploadValue = it.trim()                                        Log.d("fast", uploadValue)                                        // Создаем объект SpeedInfo                                        val speedInfo = SpeedTestData(                                            speedValue,                                            latencyValue,                                            bufferBloatValue,                                            uploadValue                                        )                                        // Отправляем данные на сервер с помощью Retrofit                                        lifecycleScope.launch(Dispatchers.IO) {                                            val response = api.postSpeedTestData(speedInfo)                                            request(response)                                        }                                    }                                }                            }                        }                    }                }            }        }    }}    fun request(response: Response<ResponseBody>){        try {            if(response.isSuccessful){                Log.d("fast","good")            }else{                Log.d("fast","${response.code()}")            }        }catch (e: HttpException){            Log.d("fast",e.message())        }catch (e:Throwable){            Log.d("fast","error")        }    }}