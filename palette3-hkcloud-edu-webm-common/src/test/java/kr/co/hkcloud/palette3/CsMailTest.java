package kr.co.hkcloud.palette3;

public class CsMailTest {
    /**
     * kr.co.hkcloud.palette3.cron.app.CronServiceImpl 고객사 이메일 수집 샘플 코드 백업.
     * 
     */
    
    
//    /**
//     * 고객사 상담 문의 메일 수집
//     */
//    @Override
//    @Transactional(readOnly = false)
//    public void collectCsMail() throws TelewebAppException {
//        String user = "hjh@hkcloud.co.kr";
//        String password = "gothf1010!!";
//        String host = "mail.hkcloud.co.kr";
//        String port = "143";
//        String protocol = "imap";
//        String sslYn = "N";
//        String custcoId = "1";
//        String aspCustKey = "pallet3";
//        RepositoryTaskTypeCd taskTypeCd = RepositoryTaskTypeCd.csmail;
//        String saveRepository = "P:" + File.separator + "repository" + File.separator + "web";
////        String saveFilePath = saveRepository + File.separator + taskTypeCd;
////        String saveInlineImagePath = saveRepository + File.separator + "images" + File.separator + taskTypeCd;
////        String linkInlineImage = "/upload/images/" + taskTypeCd;
//        
//        // Setup properties for the mail session.
//        Properties props = new Properties();
//        if ( "Y".equals(sslYn) ) {
//            props.put("mail.imaps.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
//            props.put("mail.imaps.socketFactory.fallback", "false");
//            props.put("mail.imaps.socketFactory.port", port);
//        }
//
//        props.put("mail.store.protocol", protocol);
//        props.put("mail.imaps.host", host);
//        props.put("mail.imaps.port", port);
//        
//        Store store = null;
//        Folder inbox = null;
//        
//        String mailUid = null;
//        String mailSubject = null;
//        try {
//            // Creating mail session.
//            Session session = Session.getDefaultInstance(props, null);
//    
//            store = session.getStore(protocol);
//            store.connect(host, user, password);
//    
//            // 5. Get folder and open the INBOX folder in the store.
//            inbox = store.getFolder("INBOX");
//            inbox.open(Folder.READ_ONLY);
////            SearchTerm dateTerm = null;
//            String lastDateStr = "20231207000000";
//            SearchTerm startDateTerm = null;
//    
//            if (lastDateStr != null) {
//                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");         // 문자열 -> Date        
//                Date lastDate = formatter.parse(lastDateStr);
//                Calendar c = Calendar.getInstance();
//                c.setTime(lastDate);
//                startDateTerm = new ReceivedDateTerm(ComparisonTerm.GE, c.getTime());
//                log.debug("search ::: " + c);
////                dateTerm = new AndTerm(startDateTerm, endDateTerm);
//            }
//    
//            // 최근 1일 동안의 메일 검색
//            //        long oneDayInMillis = 24 * 60 * 60 * 1000 * 1; // 1일의 밀리초
//            //        long sinceTime = System.currentTimeMillis() - oneDayInMillis;
//            //        SearchTerm searchTerm = new ReceivedDateTerm(ComparisonTerm.GT, new java.util.Date(sinceTime));
//    
//            Message[] messages = null;
//    
//            log.debug("inbox.search(dateTerm) ::: ");
//            //            messages = inbox.getMessages(); //전체
//            messages = inbox.search(startDateTerm);
//            log.debug("inbox.search(searchTerm, messages) messages.length ::: " + messages.length);
//    
//            if (messages != null) {
//                // 6. Retrieve the messages from the folder.
//                for (Message message : messages) {
//                    mailUid = message.getHeader("message-id")[0];
//                    mailUid = mailUid.replace("<", "").replace(">", "");
//                    mailSubject = message.getSubject();
//                    String mailReceiveDate = DateFormatUtils.format(message.getReceivedDate(), "yyyyMMddHHmmss");
//                    String from = getAddress(message.getFrom());
//                    String recipientTo = getAddress(message.getRecipients(RecipientType.TO));
//                    String recipientCc = getAddress(message.getRecipients(RecipientType.CC));
//                    String recipientBcc = getAddress(message.getRecipients(RecipientType.BCC));
//                    
//                    String mailContentHtml = "";
//                    String mailContentText = "";
//                    String attachFiles = "";
//                    String fileGroupKey = "";
//                    //본문 내 이미지 정보 - html 내 src="cid:image001.xxxx" 형태를 다운 받은 이미지 주소로 대체하기 위함.
//                    List<Map<String, String>> inlineImages = new ArrayList<Map<String, String>>();
//                    if(true || "1b6db93f2-2e79-4b77-94aa-a7084f051645@hkcloud.co.kr".equals(mailUid)) {
//                        
//                        if (message.isMimeType("multipart/*")) {
//                            MimeMultipart multipart = (MimeMultipart) message.getContent();
//                            log.debug("multipart.getCount() ::: " + multipart.getCount());
//                            for (int i = 0; i < multipart.getCount(); i++) {
//                                MimeBodyPart bodyPart = (MimeBodyPart)multipart.getBodyPart(i);
//                                log.debug("bodyPart(" + i + ") ::: " + bodyPart.getContentType() + ", " + bodyPart.getDisposition());
//                                if (Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition())) {
//                                    
//                                    String actlFileNm = MimeUtility.decodeText(bodyPart.getFileName());
//                                    String fileExtent = actlFileNm.substring(actlFileNm.lastIndexOf("."));
//                                    
//                                    FilePropertiesResponse filePropertiesResponse = new FilePropertiesResponse();
//                                    filePropertiesResponse.setCustcoId(custcoId);
//                                    filePropertiesResponse.setUserId(2);
//                                    filePropertiesResponse.setTrgtTypeCd(RepositoryTrgtTypeCd.FILE);
//                                    filePropertiesResponse.setPathTypeCd(RepositoryPathTypeCd.files);
//                                    filePropertiesResponse.setTaskTypeCd(RepositoryTaskTypeCd.csmail);
//                                    filePropertiesResponse.setDir(Paths.get(
//                                        filePropertiesResponse.getPathTypeCd() + "/" + TenantContext.getCurrentTenant() + "_" + TenantContext.getCurrentCustco() + "/"
//                                            + filePropertiesResponse.getTaskTypeCd()));
//                                    
//                                    
//                                    //비즈구분에 따른 경로 규칙 가져오기
//                                    Path taskTypeCdFilePath = new File(env.getString("file.repository.root-dir","") + File.separator + filePropertiesResponse.getDir() ).toPath();
//                                    log.debug("taskTypeCdFilePath :{}", taskTypeCdFilePath);
//
//                                    //파일 경로 규칙 가져오기
//                                    Path ruleStorePath = fileRuleUtils.getRuleStorePath(filePropertiesResponse);
//
//                                    //저장할 파일명과 확장자 규칙 가져오기
//                                    RuleFilenameAndExtentResponse ruleFilenameAndExtentResponse = fileRuleUtils.getRuleFilenameAndExtent(filePropertiesResponse, actlFileNm, fileExtent);
//                                    String storeFilenameExtent = ruleFilenameAndExtentResponse.getRuleFilenameExtent();
//
//                                    //전체 경로 생성
//                                    Path storeLocaltionPath = taskTypeCdFilePath.resolve(ruleStorePath).toAbsolutePath().normalize();
//                                    Path storeLocaltionFull = storeLocaltionPath.resolve(storeFilenameExtent).toAbsolutePath().normalize();
//                                    
//                                    
//                                    File fileDir = new File(storeLocaltionPath.toUri());
//                                    if(!fileDir.isDirectory()) {
//                                        fileDir.mkdirs();
//                                    }
////                                    
//                                    bodyPart.saveFile(storeLocaltionFull.toString());
//                                    
//                                    long fileSize = Files.size(storeLocaltionFull);
//                                    String fileMimeType = new Tika().detect(storeLocaltionFull);
//                                    fileGroupKey = fileRuleUtils.ifCreatFileGroupKey(FileUploadRequests.builder()
//                                        .fileGroupKey(fileGroupKey)
//                                        .build());
//                                    
//                                    ///////////////////////////////////////////////////////////////////////////
//                                    log.debug("///////////////////////////////////////////////////////////////////////////");
//                                    log.debug("filePropertiesResponse ::: " + filePropertiesResponse.toString());
//                                    log.debug("ruleFilenameAndExtentResponse ::: " + ruleFilenameAndExtentResponse.toString());
//                                    log.debug("storeLocaltionFull ::: " + storeLocaltionFull);
//                                    log.debug("bodyPart.getContentType() ::: " + bodyPart.getDataHandler().getContentType());
//                                    log.debug("fileSize::: " + fileSize);
//                                    log.debug("fileMimeType ::: " + fileMimeType);
//                                    log.debug("fileGroupKey ::: " + fileGroupKey);
//                                    log.debug("///////////////////////////////////////////////////////////////////////////");
//                                    ///////////////////////////////////////////////////////////////////////////
//                                    
//                                    insertFile(filePropertiesResponse, ruleFilenameAndExtentResponse, ruleStorePath, fileGroupKey, fileSize, fileMimeType);
//                                    
//                                    
//                                } else if(bodyPart.isMimeType("image/*")) {
//                                    FilePropertiesResponse imagePropertiesResponse = new FilePropertiesResponse();
//                                    imagePropertiesResponse.setPathTypeCd(RepositoryPathTypeCd.images);
//                                    imagePropertiesResponse.setTaskTypeCd(RepositoryTaskTypeCd.csmail);
//                                    imagePropertiesResponse.setDir(Paths.get(
//                                        imagePropertiesResponse.getPathTypeCd() + "/" + TenantContext.getCurrentTenant() + "_" + TenantContext.getCurrentCustco() + "/"
//                                            + imagePropertiesResponse.getTaskTypeCd() + "/" + mailUid));
//                                    
//                                    Path taskTypeCdImagePath = new File(env.getString("file.repository.root-dir","") + File.separator + imagePropertiesResponse.getDir() ).toPath();
//                                    
//                                    //Part.INLINE 이미지 - 본문 내 이미지가 INLINE 으로 들어오진 않아서 bodyPart.isMimeType("image/*") 으로 체크.
//                                    String cid = bodyPart.getContentID().replace("<", "").replace(">", "");
//                                    String link = "/upload/" + imagePropertiesResponse.getDir().toString().replaceAll(Pattern.quote("\\"), "/") + "/" + cid;
//                                    
//                                    Path storeLocaltionPath = taskTypeCdImagePath.toAbsolutePath().normalize();
//                                    Path storeLocaltionFull = storeLocaltionPath.resolve(cid).toAbsolutePath().normalize();
//                                    File inlineImageDir = new File(storeLocaltionPath.toUri());
//                                    if(!inlineImageDir.isDirectory()) {
//                                        inlineImageDir.mkdirs();
//                                    }
//                                    bodyPart.saveFile(storeLocaltionFull.toString());
//                                    
//                                    Map<String, String> inlineImage = new HashMap<String, String>();
//                                    inlineImage.put("cid", cid);
//                                    inlineImage.put("link", link);
//                                    inlineImages.add(inlineImage);
//                                } else {
//                                    //메일 본문 - multipart/*
//                                    mailContentHtml = getContentFromMimeBodyPart(bodyPart, "text/html");
//                                    mailContentText = getContentFromMimeBodyPart(bodyPart, "text/plain");
//                                }
//                            }
//                        } else {
//                            //메일 본문 - text/plain, text/html
//                            mailContentHtml = message.getContent().toString();
//                            log.debug("else in " + mailContentHtml);
//                        }
//                        
//                        log.debug("==================================================================================================");
////                        log.debug("flag ::: " + message.getFlags());
////                        log.debug("getContentType() ::: " + message.getContentType());
////                        log.debug("CUSTCO_ID ::: " + custcoId);
//                        log.debug("mailUid ::: " + mailUid);
////                        log.debug("mailReceiveDate ::: " + mailReceiveDate);
//                        log.debug("mailSubject ::: " + mailSubject);
////                        log.debug("from ::: " + from);
////                        log.debug("recipientTo ::: " + recipientTo);
////                        log.debug("recipientCc ::: " + recipientCc);
////                        log.debug("recipientBcc ::: " + recipientBcc);
////                        log.debug("mailContentHtml ::: " + mailContentHtml);
////                        log.debug("mailContentText ::: " + mailContentText);
//                        
//                        log.debug("==================================================================================================\n\n\n\n\n");
//                        log.debug("inlineImages.size ::: " + inlineImages.size());
//                        mailContentHtml = convertContentHtml(mailContentHtml, inlineImages);
//                        
//                        TelewebJSON csMailParams = new TelewebJSON();
//                        csMailParams.setString("CUSTCO_ID", custcoId);
//                        csMailParams.setString("MAIL_HOST", host);
//                        csMailParams.setString("MAIL_UID", mailUid);
//                        csMailParams.setString("MAIL_RECEIVE_DATE", mailReceiveDate);
//                        csMailParams.setString("MAIL_SUBJECT", mailSubject);
//                        csMailParams.setString("MAIL_CONTENT_HTML", mailContentHtml);
//                        csMailParams.setString("MAIL_CONTENT_TEXT", mailContentText);
//                        csMailParams.setString("MAIL_FROM", from);
//                        csMailParams.setString("MAIL_TO", recipientTo);
//                        csMailParams.setString("MAIL_CC", recipientCc);
//                        csMailParams.setString("MAIL_BCC", recipientBcc);
//                        csMailParams.setString("FILE_GROUP_KEY", fileGroupKey);
//                        csMailParams.setString("RGTR_ID", "2");
//                        
//                        mobjDao.insert(namespace, "insertCsMail", csMailParams);
//                    }
//                    
//                }
//            }
//            inbox.close(false);
//            store.close();
//        } catch(Exception e) {
//            log.error("ERROR collectCsMail ::: " + mailUid + " ::: " + mailSubject + "::: " + e.toString());
//            // Close folder and close store.
//            try {
//                if(inbox != null && inbox.isOpen()) inbox.close(false);
//                if(store != null && inbox.isOpen()) store.close();
//            } catch (MessagingException ee) {
//                // TODO Auto-generated catch block
//                log.error("ERROR collectCsMail close ::: " + ee.toString());
//            }
//        } finally {
//            //Close folder and close store.
//            try {
//                if(inbox != null && inbox.isOpen()) inbox.close(false);
//                if(store != null && inbox.isOpen()) store.close();
//            } catch (MessagingException e) {
//                // TODO Auto-generated catch block
//                log.error("ERROR collectCsMail ::: " + e.toString());
//            }
//            
//        }
//
//        
//
//    }
//    
//    /**
//     * 메일 주소 가져오기
//     * @param recipientsAddress
//     * @return
//     */
//    private String getAddress(Address[] recipientsAddress) {
//        String address = "";
//        try {
//            if(recipientsAddress != null) {
//                for(Address recipient : recipientsAddress) {
//                    if(!"".equals(address)) {
//                        address += ",";
//                    }
//                    address += MimeUtility.decodeText(recipient.toString());
//                }
//            }
//        } catch(Exception e) {
//            log.error("ERROR getAddress : " + e.toString());
//        }
//        
//        return address;
//    }
//    
//    /**
//     * 메일 본문 가져오기.
//     * @param bodyPart
//     * @return
//     * @throws Exception
//     */
//    private String getContentFromMimeBodyPart(
//        MimeBodyPart bodyPart, String mimeType) throws Exception{
//        boolean isMultipart = false;
//        String content = "";
//        
//        MimeMultipart multipart = null;
//        if ( bodyPart.getContent() instanceof MimeMultipart ) {
//            multipart = getMultipart((MimeMultipart)bodyPart.getContent());
//            isMultipart = true;
//        }
//        
//        if( isMultipart ) {
//            int count = multipart.getCount();
//            //text/html 이 뒤에 있어 for문 역순으로 돌림 
//            for ( int i = count - 1; i >= 0; i-- ) {
//              MimeBodyPart childBodyPart = (MimeBodyPart)multipart.getBodyPart(i);
//              content = getMailContent(childBodyPart, mimeType);
//              if(!"".equals(content)) {
//                  break;
//              }
//            }
//        } else {
//            content = getMailContent(bodyPart, mimeType);
//        }
//        
//        return content ;
//    }
//    
//    
//    private MimeMultipart getMultipart(MimeMultipart multipart) {
//        try {
//            int count = multipart.getCount();
//            for ( int i = 0; i < count; i++ ) {
//                BodyPart childBodyPart = multipart.getBodyPart(i);
//                if ( childBodyPart.getContent() instanceof MimeMultipart ){
//                    return getMultipart((MimeMultipart)childBodyPart.getContent());
//                }
//            }
//        } catch(Exception e) {
//            log.error("ERROR getMultipart : " + e.toString());
//        }
//        return multipart;
//    }
//    
//    private String getMailContent(MimeBodyPart bodyPart, String mimeType) {
//        String content = "";
//        try {
//            if ( bodyPart.isMimeType(mimeType) ) {
//                content = (String)bodyPart.getContent();
//            }
//            
//        } catch(Exception e) {
//            log.error("ERROR getMailContent : " + e.toString());
//        }
//        return content;
//    }
//    
//    
//    private String convertContentHtml(String html, List<Map<String, String>> inlineImages) {
//        //HTML 본문 내 이미지 정보 - html 내 src="cid:image001.xxxx" 형태를 다운 받은 이미지 주소로 대체하기 위함.
//        if(!"".equals(html) && inlineImages.size() > 0) {
//            for(Map<String, String> inlineImage : inlineImages) {
//                html = html.replaceAll("src=\"cid:" + inlineImage.get("cid") + "\"", "src=\"" + java.util.regex.Matcher.quoteReplacement(inlineImage.get("link")) + "\"");
//            }
//        }
//        //html Content XSS 처리(script, link)
//        //mailContentHtml
//        
//        return html;
//    }
//    
//    
//    @Transactional(readOnly = false)
//    private void insertFile(FilePropertiesResponse filePropertiesResponse, RuleFilenameAndExtentResponse ruleFilenameAndExtentResponse, Path ruleStorePath, String fileGroupKey, long fileSize, String fileMimeType) {
//     // @formatter:off
//       
//        FileDbMngInsertRequest fileDbMngInsertRequest = 
//            FileDbMngInsertRequest.builder()
//                                  .fileGroupKey(fileGroupKey)
//                                  .fileAcsTypeCd(FileAccessType.PRIVATE)
//                                  .taskTypeCd(filePropertiesResponse.getTaskTypeCd())
//                                  .trgtTypeCd(filePropertiesResponse.getTrgtTypeCd())
//                                  .pathTypeCd(filePropertiesResponse.getPathTypeCd())
//                                  .mimeTypeCd(fileMimeType)
//                                  .actlFileNm(ruleFilenameAndExtentResponse.getActlFileNm())
//                                  .strgFileNm(ruleFilenameAndExtentResponse.getRuleFilenameExtent())
//                                  .fileSz(fileSize)
//                                  .filePath(ruleStorePath.toString().replaceAll(Matcher.quoteReplacement(File.separator), "/"))
//                                  .fileExtn(ruleFilenameAndExtentResponse.getCleanFileExtent())
//                                  .custcoId(filePropertiesResponse.getCustcoId())
//                                  .userId(filePropertiesResponse.getUserId())
//                                  .rgtrId(filePropertiesResponse.getUserId())
//                                  .build();
//        log.debug("fileDbMngInsertRequest :{}", fileDbMngInsertRequest);
//        
//      //DB 삽입 처리
//        fileDbMngService.insert(fileDbMngInsertRequest);
//    }
}
