const express = require("express");
const https = require("https");
const fs = require("fs");
const bodyParser = require("body-parser");
const syncRequest = require("sync-request");
var moment = require('moment');
var cron = require('node-cron');

var today = "";
var startHms = "";
var endHms = "";
var lastNaverJson = "";
var lastKakaoJson = "";

//scouter 채팅데이터 수집
function collectScouter() {
    /////////////////////////////////////////////////
    //날짜 계산 일단. 임시로.. 
    var beforedate = today;
    var d1 = moment(new Date()).format('YYYYMMDDHHmmss');
    today = d1.substring(0,8);
    endHms = d1.substring(8,14);

    var date1 = new Date(d1.replace(/^(\d{4})(\d\d)(\d\d)(\d\d)(\d\d)(\d\d)$/, '$1-$2-$3 $4:$5:$6'));    
    var date2 = date1.setSeconds(date1.getSeconds() - 10);   //10초 빼기

    var d2 = moment(new Date( date2 )).format('YYYYMMDDHHmmss');
    startHms = d2.substring(8,14);

    console.log( "today : " + today );
    console.log( "startHms : " + startHms );
    console.log( "endHms : " + endHms );
    //여기까지.
    /////////////////////////////

    //scouter 수집데이터 중 네이버 데이터 수집 하여 파일저장..
    var naverRes = syncRequest('GET','http://localhost:6180/scouter/v1/xlog-data/search/'+ today +'?startHms='+ startHms +'&endHms='+ endHms +'&service=*/navertalktalk/*&userAgent=navertalk/v1',{});
    var naverJson = JSON.parse(naverRes.getBody('utf8'));
    if( naverJson.result.length > 0) {
        lastNaverJson = JSON.stringify(naverJson.result[0]);
        fs.writeFile("./naver.dat", lastNaverJson, (err) => {});
    }
    console.log(moment(new Date()).format('YYYY-MM-DD HH:mm:ss a') + ' :: 네이버톡톡 수집건 - ' + naverJson.result.length);

    //scouter 수집데이터 중 카카오 데이터 수집 하여 파일저장..
    var kakaoRes = syncRequest('GET','http://localhost:6180/scouter/v1/xlog-data/search/'+ today +'?startHms='+ startHms +'&endHms='+ endHms +'&service=/message<POST>&userAgent=AHC/20',{});
    var kakaoJson = JSON.parse(kakaoRes.getBody('utf8'));
    if( kakaoJson.result.length > 0) {
        lastKakaoJson = JSON.stringify(kakaoJson.result[0]);
        fs.writeFile("./kakao.dat", lastKakaoJson, (err) => {});
    }
    console.log(moment(new Date()).format('YYYY-MM-DD HH:mm:ss a') + ' :: 카카오톡 수집건 - ' + kakaoJson.result.length);
}

//10초마다 채팅데이터 수집 스케줄링.
cron.schedule('*/10 * * * * *', () => {  
    collectScouter();
});


const HTTPS_PORT = 4443;
const options = {
    key: fs.readFileSync("./ssl/key.pem"),
    cert: fs.readFileSync("./ssl/cert.pem"),
    ca: fs.readFileSync("./ssl/TuringSignCA.pem"),
    passphrase: 'hkcloud1!'
};

const app = express();
app.use(bodyParser.json());

app.get("/", (req, res) => {   

    res.writeHead(200, {'Content-Type':'text/html; charset=utf-8'});
    res.write("<!DOCTYPE html>");
    res.write("<html>");
    res.write("<head>");
    res.write("<meta http-equiv='refresh' content='5'>");
    res.write("</head>");
    res.write("<body>");
    res.write("<h4 style='margin-top:-10px'>Chat Health Check - 검색조건 :: "+ today + " " + startHms + " ~ " + endHms + "  </h4>");
    res.write("<table border=1 width=100% style='margin-top:-20px'>");
    res.write(" <tr>");
    res.write("     <th width=10% align=left>서비스</th>");
    res.write("     <th align=left>최종호출일시</th>");
    res.write("     <th width=10% align=left>ipAddr</th>");    
    res.write("     <th width=20% align=left>service</th>");
    res.write("     <th width=10% align=left>userAgent</th>");
    res.write("     <th width=5% align=left>elapsed</th>");
    res.write("     <th width=5% align=left>cpu</th>");
    res.write("     <th width=5% align=left>sqlCount</th>");
    res.write("     <th width=5% align=left>sqlTime</th>");
    res.write(" </tr>");

    var d1 = new Date();

    var naverData = (lastNaverJson != "")?lastNaverJson:fs.readFileSync('./naver.dat',{ encoding: 'utf8', flag: 'r' });
    try {
        var json = JSON.parse(naverData);
        var d2 = new Date(parseInt(json.endTime));
        const diffMSec = d1.getTime() - d2.getTime();
        const diffMin = diffMSec / (60 * 1000);
        const diffSec = diffMSec / (1000);
        res.write(" <tr>");
        res.write("     <td>네이버톡톡</td>");
        res.write("     <td style='font-weight:bold;color:red'>"+ moment(new Date(parseInt(json.endTime))).format('YYYY-MM-DD HH:mm:ss') +" , 마지막호출(" + diffMin.toFixed(2) +"분전, "+ diffSec.toFixed(2) +"초전)</td>");
        res.write("     <td>"+ json.ipAddr +"</td>");
        res.write("     <td>"+ json.service +"</td>");
        res.write("     <td>"+ json.userAgent +"</td>");
        res.write("     <td>"+ json.elapsed +"</td>");
        res.write("     <td>"+ json.cpu +"</td>");
        res.write("     <td>"+ json.sqlCount +"</td>");
        res.write("     <td>"+ json.sqlTime +"</td>");
        res.write(" </tr>");
    }catch(e){
        res.write(" <tr>");
        res.write("     <td colspan=7 >네이버톡톡 호출이력 없음.</td>");
        res.write(" </tr>");
    }
    var kakaoData = (lastKakaoJson != "")?lastKakaoJson:fs.readFileSync('./kakao.dat',{ encoding: 'utf8', flag: 'r' });
    try {
        var json = JSON.parse(kakaoData);
        var d2 = new Date(parseInt(json.endTime));
        const diffMSec = d1.getTime() - d2.getTime();
        const diffMin = diffMSec / (60 * 1000);
        const diffSec = diffMSec / (1000);
        res.write(" <tr>");
        res.write("     <td>카카오톡</td>");
        res.write("     <td style='font-weight:bold;color:red;'>"+ moment(new Date(parseInt(json.endTime))).format('YYYY-MM-DD HH:mm:ss')  +" , 마지막호출(" + diffMin.toFixed(2) +"분전, "+ diffSec.toFixed(2) +"초전)</td>");
        res.write("     <td>"+ json.ipAddr +"</td>");
        res.write("     <td>"+ json.service +"</td>");
        res.write("     <td>"+ json.userAgent +"</td>");
        res.write("     <td>"+ json.elapsed +"</td>");
        res.write("     <td>"+ json.cpu +"</td>");
        res.write("     <td>"+ json.sqlCount +"</td>");
        res.write("     <td>"+ json.sqlTime +"</td>");
        res.write(" </tr>");
    }catch(e){
        res.write(" <tr>");
        res.write("     <td colspan=7 >카카오톡 호출이력 없음.</td>");
        res.write(" </tr>");
    }

    res.write("</table>");
    res.write("</body>");
    res.write("</html>");
    res.end();
    //res.json({ message: `Server is running on port ${HTTPS_PORT}` });
});
console.log("====================================");
console.log(moment(new Date()).format('YYYY-MM-DD HH:mm:ss a') + ' :: Server starting... - ');

// Create an HTTPS server.
https.createServer(options, app).listen(HTTPS_PORT);