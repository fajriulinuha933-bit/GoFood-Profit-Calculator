package com.example.gofoodprofit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.NumberFormat
import java.util.Locale

private val navy = Color(0xFF17324D)
private val green = Color(0xFF16865B)
private fun rp(n: Double) = NumberFormat.getCurrencyInstance(Locale("id","ID")).format(n).replace(",00","")
private fun num(s: String) = s.replace(".","").replace(",",".").toDoubleOrNull() ?: 0.0

class MainActivity : ComponentActivity() {
 override fun onCreate(savedInstanceState: Bundle?) { super.onCreate(savedInstanceState); setContent { MaterialTheme(colorScheme = lightColorScheme(primary=navy, secondary=green)) { App() } } }
}

@Composable fun App() {
 var price by remember { mutableStateOf("28500") }
 var cost by remember { mutableStateOf("15000") }
 var feePct by remember { mutableStateOf("20") }
 var menuOff by remember { mutableStateOf("0") }
 var voucher by remember { mutableStateOf("0") }
 var subsidy by remember { mutableStateOf("20") }
 var feeBase by remember { mutableStateOf("Harga jual") }
 var period by remember { mutableStateOf("Manual") }

 val p=num(price).coerceAtLeast(0.0); val c=num(cost).coerceAtLeast(0.0)
 val d=num(menuOff).coerceAtLeast(0.0); val v=num(voucher).coerceAtLeast(0.0)
 val s=num(subsidy).coerceIn(0.0,100.0)/100.0
 val merchantVoucher=v*(1-s)
 val netBase=(p-d-merchantVoucher).coerceAtLeast(0.0)
 val commission=(if(feeBase=="Harga jual") p else netBase)*num(feePct).coerceIn(0.0,100.0)/100.0
 val received=(p-d-merchantVoucher-commission).coerceAtLeast(0.0)
 val profit=received-c
 val margin=if(p>0) profit/p*100 else 0.0

 Column(Modifier.fillMaxSize().background(Color(0xFFF3F6F8)).verticalScroll(rememberScrollState()).padding(16.dp)) {
  Text("GoFood Profit", fontSize=27.sp, fontWeight=FontWeight.Bold, color=navy)
  Text("Kalkulator keuntungan usaha • Offline", color=Color.Gray, fontSize=13.sp)
  Spacer(Modifier.height(14.dp))
  Card(shape=RoundedCornerShape(20.dp), colors=CardDefaults.cardColors(containerColor=navy)) {
   Column(Modifier.fillMaxWidth().padding(18.dp)) {
    Text("ESTIMASI UANG BERSIH DITERIMA", color=Color(0xFFD7E5F1), fontSize=11.sp)
    Text(rp(received), color=Color.White, fontSize=28.sp, fontWeight=FontWeight.Bold)
    Spacer(Modifier.height(12.dp))
    Row(Modifier.fillMaxWidth(), horizontalArrangement=Arrangement.SpaceBetween) {
     Column { Text("Keuntungan", color=Color.LightGray, fontSize=12.sp); Text(rp(profit), color=if(profit>=0) Color(0xFF8EF0BC) else Color(0xFFFFB4AB), fontSize=18.sp, fontWeight=FontWeight.Bold) }
     Column(horizontalAlignment=Alignment.End) { Text("Margin", color=Color.LightGray, fontSize=12.sp); Text("%.1f%%".format(Locale.US,margin), color=Color.White, fontSize=18.sp, fontWeight=FontWeight.Bold) }
    }
   }
  }
  Heading("Data produk")
  Input("Harga jual ke pelanggan (Rp)",price){price=it}
  Input("Modal / biaya produk (Rp)",cost){cost=it}
  Heading("Potongan & promo")
  Input("Diskon menu ditanggung merchant (Rp)",menuOff){menuOff=it}
  Input("Voucher pelanggan (nilai diskon Rp)",voucher){voucher=it}
  Input("Subsidi GoFood atas voucher (%)",subsidy){subsidy=it}
  Input("Komisi / biaya platform (%)",feePct){feePct=it}
  Text("Dasar perhitungan komisi", color=navy, fontWeight=FontWeight.SemiBold, modifier=Modifier.padding(top=8.dp,bottom=5.dp))
  Row(horizontalArrangement=Arrangement.spacedBy(8.dp)) {
   FilterChip(selected=feeBase=="Harga jual",onClick={feeBase="Harga jual"},label={Text("Harga jual")})
   FilterChip(selected=feeBase=="Setelah diskon",onClick={feeBase="Setelah diskon"},label={Text("Setelah diskon")})
  }
  Heading("Periode promo cepat")
  Text("Preset mengisi subsidi; minimum transaksi ditampilkan sebagai pengingat.",fontSize=12.sp,color=Color.Gray)
  Row(horizontalArrangement=Arrangement.spacedBy(6.dp)) {
   FilterChip(selected=period=="21–30 Nov",onClick={period="21–30 Nov";subsidy="20"},label={Text("21–30 Nov")})
   FilterChip(selected=period=="1–31 Des",onClick={period="1–31 Des";subsidy="25"},label={Text("1–31 Des")})
   FilterChip(selected=period=="Manual",onClick={period="Manual"},label={Text("Manual")})
  }
  if(period!="Manual") Text(if(period=="21–30 Nov") "Minimum transaksi Rp35.000 • subsidi 20%" else "Minimum transaksi Rp40.000 • subsidi 25%",color=green,fontSize=12.sp)
  Heading("Rincian perhitungan")
  RowResult("Harga jual",rp(p)); RowResult("Diskon menu merchant","− "+rp(d))
  RowResult("Bagian voucher ditanggung merchant","− "+rp(merchantVoucher))
  RowResult("Komisi platform","− "+rp(commission))
  HorizontalDivider(Modifier.padding(vertical=7.dp))
  RowResult("Uang bersih diterima",rp(received),true)
  RowResult("Modal produk","− "+rp(c))
  RowResult("Keuntungan bersih",rp(profit),true,if(profit>=0) green else Color(0xFFB3261E))
  Spacer(Modifier.height(12.dp))
  Text("Catatan: simulasi menganggap subsidi dihitung dari nilai voucher dan diskon menu ditanggung merchant. Komisi dapat dihitung dari harga jual atau setelah diskon. Biaya lain, minimum transaksi, pembulatan, dan ketentuan program GoFood perlu dicocokkan dengan rincian transaksi GoFood Merchant.",fontSize=11.sp,color=Color.Gray)
 }
}

@Composable fun Heading(t:String){ Text(t,fontSize=17.sp,fontWeight=FontWeight.Bold,color=navy,modifier=Modifier.padding(top=17.dp,bottom=7.dp)) }
@Composable fun Input(label:String,value:String,onChange:(String)->Unit){
 OutlinedTextField(value=value,onValueChange={onChange(it.filter{ch->ch.isDigit()||ch=='.'||ch==','})},label={Text(label)},singleLine=true,modifier=Modifier.fillMaxWidth().padding(bottom=7.dp),keyboardOptions=KeyboardOptions(keyboardType=KeyboardType.Decimal))
}
@Composable fun RowResult(label:String,value:String,bold:Boolean=false,color:Color=Color(0xFF263238)){
 Row(Modifier.fillMaxWidth().padding(vertical=4.dp),horizontalArrangement=Arrangement.SpaceBetween){
  Text(label,modifier=Modifier.weight(1f),fontSize=13.sp,color=color,fontWeight=if(bold) FontWeight.Bold else FontWeight.Normal)
  Text(value,fontSize=13.sp,color=color,fontWeight=if(bold) FontWeight.Bold else FontWeight.Medium)
 }
}
