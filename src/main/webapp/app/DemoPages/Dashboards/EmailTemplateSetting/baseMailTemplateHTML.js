const defaultHTMLContent = `
<html xmlns="http://www.w3.org/1999/xhtml" lang="en-GB">
   <head>
      <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
      <title>{{ subject }}</title>
      <meta name="viewport" content="width=device-width, initial-scale=1" />
      <link href="https://fonts.googleapis.com/css?family=Lato:400,700" rel="stylesheet">
      <style type="text/css"> a[x-apple-data-detectors] { color: inherit !important; } </style>
   </head>
   <body style="margin: 0; padding: 0; font-family: Lato, Helvetica,Arial,sans-serif; color: #495661; color: {{ text_color }};" >
      <table role="presentation" border="0" cellpadding="0" cellspacing="0" width="100%" >
         <tbody style="width: 100%">
            <tr>
               <td>
                  <table align="center" border="0" cellpadding="0" cellspacing="0" width="98%" style="border-collapse: collapse; max-width: 600px" >
                     <tbody>
                        <tr>
                           <td align="center" bgcolor="">
                              <table width="100%" border="0" cellpadding="0" cellspacing="0" >
                                 <tbody>
                                    <tr>
                                       <td>
                                          <table style="width: 100%" border="0" cellpadding="0" cellspacing="0" >
                                             <tbody>
                                                <tr>
                                                   <td height="30" style="height: 30px"></td>
                                                </tr>
                                                {% if logo_url != null and logo_url.size != 0 %} 
                                                <tr>
                                                   <td align={{logoAlignment}} valign="top"> <img width="{{logoWidth}}" height="{{logoHeight}}" src="{{ logo_url }}" style=" border: none; object-fit: contain; width: {{logoWidth}}px; height: {{logoHeight}}px; " /> </td>
                                                </tr>
                                                {% endif %} {% if text_image_url != null and text_image_url.size != 0 %} 
                                                <tr>
                                                   <td height="35" style="height: 35px"> </td>
                                                </tr>
                                                <tr>
                                                   <td align={{thanksImageAlignment}}> <img src="{{ text_image_url }}" width={{thanksImageWidth}}px height={{thanksImageHeight}}px /> </td>
                                                </tr>
                                                {% endif %} 
                                             </tbody>
                                          </table>
                                       </td>
                                    </tr>
                                 </tbody>
                              </table>
                           </td>
                        </tr>
                        <tr>
                           <td bgcolor="#ffffff">
                              <table border="0" cellpadding="0" cellspacing="0" width="100%" style="border-collapse: collapse" >
                                 <tbody>
                                    <tr>
                                       <td>
                                          <table border="0" cellpadding="0" cellspacing="0" width="100%" >
                                             <tbody>
                                                <tr>
                                                   <td height="35" class="em_height" style="height: 35px" > </td>
                                                </tr>
                                                <tr>
                                                   <td>
                                                      <table border="0" cellpadding="0" cellspacing="0" width="100%">
                                                         <tbody>
                                                            <tr>
                                                               {% if heading_image_url != null and heading_image_url.size != 0 %} 
                                                               <td width="30" style="width: 30px"> <img src={{ heading_image_url }} width="30" style="width: 30px"> </td>
                                                               <td width="10" style="width: 10px"></td>
                                                               {% endif %} 
                                                               <td style="font-size: 16px; font-family: Lato, Helvetica, Arial, sans-serif; line-height: 1.5; letter-spacing: 0.3px; font-weight: bold; color: #202E3A; color: {{ header_text_color }};">{{ heading }}</td>
                                                            </tr>
                                                         </tbody>
                                                      </table>
                                                   </td>
                                                </tr>
                                                <tr>
                                                   <td height="15" style="height: 15px"></td>
                                                </tr>
                                                <tr>
                                                   <td style="font-size: 14px; font-family: Lato, Helvetica, Arial, sans-serif; line-height: 1.5; letter-spacing: 0.3px; color: #495661; color: {{ text_color }};" > {{ body_content | newline_to_br }} </td>
                                                </tr>
                                                <tr>
                                                   <td height="25" style="height: 25px;"></td>
                                                </tr>
                                                {% if manage_subscription_button_text != null and manage_subscription_button_text.size != 0 %} 
                                                <tr>
                                                   <td>
                                                      <table cellspacing="0" cellpadding="0" border="0">
                                                         <tbody>
                                                            <tr>
                                                               <td style="border-radius: 5px;" bgcolor="{{ manage_subscription_button_color }}"> <a href={{ manageSubscriptionLink }} target="_blank" style="padding: 12px 24px;border: 1px solid #20AE96;border-radius: 5px;font-family: Lato, Helvetica, Arial, sans-serif;font-size: 14px;color: #ffffff;text-decoration: none;font-weight:bold;display: inline-block; color: {{ manage_subscription_button_text_color }}; border-color: {{ manage_subscription_button_color }};">{{ manage_subscription_button_text }}</a> </td>
                                                            </tr>
                                                         </tbody>
                                                      </table>
                                                   </td>
                                                </tr>
                                                {% endif %} 
                                                <tr>
                                                   <td height="35" class="em_height" style="height: 35px" ></td>
                                                </tr>
                                             </tbody>
                                          </table>
                                       </td>
                                    </tr>
                                 </tbody>
                              </table>
                           </td>
                        </tr>
                        <tr>
                           <td>
                              <table border="0" cellpadding="0" cellspacing="0" width="100%" >
                                 <tbody>
                                    <tr>
                                       <td bgcolor="#F5F7FA" style="background-color: #F5F7FA; border-radius: 20px;">
                                          <table width="100%" border="0" cellpadding="0" cellspacing="0" >
                                             <tbody>
                                                <tr>
                                                   <td>
                                                      <table width="100%" border="0" cellpadding="0" cellspacing="0" >
                                                         <tbody>
                                                            <tr>
                                                               <td colspan="7" height="20" style="height: 20px"></td>
                                                            </tr>
                                                            {% for orderItem in orderItems %} 
                                                            <tr style="">
                                                               <td width="20" style="width: 20px"></td>
                                                               <td width="70" style="width: 70px" valign="top"> <img src="{{orderItem.imageUrl}}" style="display: block; max-width: 70px; max-height: 70px; width: auto; height: auto; margin: auto;" alt="{{orderItem.title}}" /> </td>
                                                               <td width="20" style="20px;"></td>
                                                               <td>
                                                                  <table border="0" cellpadding="0" cellspacing="0" >
                                                                     <tbody>
                                                                        <tr>
                                                                           <td style="font-size: 14px; font-family: Lato, Helvetica, Arial, sans-serif; line-height: 1.5; letter-spacing: 0.3px; color: #495661; color: {{ text_color }};" > {{orderItem.title}} </td>
                                                                        </tr>
                                                                        {% if orderItem.subscriptionProduct %} 
                                                                        <tr>
                                                                           <td height="10" style="font-size: 14px; font-family: Lato, Helvetica, Arial, sans-serif; line-height: 1.5; letter-spacing: 0.3px; color: #495661; color: {{ text_color }};" > {{orderItem.sellingPlanName}} </td>
                                                                        </tr>
                                                                        {% endif %} 
                                                                        <tr>
                                                                           <td style="font-size: 14px; font-family: Lato, Helvetica, Arial, sans-serif; line-height: 1.5; letter-spacing: 0.3px; color: #495661; color: {{ text_color }};" > {{ quantity_text }}: {{orderItem.quantity}} </td>
                                                                        </tr>
                                                                     </tbody>
                                                                  </table>
                                                               </td>
                                                               <td width="20" style="20px"></td>
                                                               <td style="text-align: right; font-size: 14px; font-family:Helvetica,  Arial, sans-serif; line-height: 1.5; letter-spacing: 0.3px; color: #495661; color: {{ text_color }};" valign="top" > {{orderItem.price}} </td>
                                                               <td width="20" style="width: 20px"></td>
                                                            </tr>
                                                            <tr>
                                                               <td colspan="7" height="20" style="height: 20px"></td>
                                                            </tr>
                                                            {% endfor %} 
                                                         </tbody>
                                                      </table>
                                                   </td>
                                                </tr>
                                             </tbody>
                                          </table>
                                       </td>
                                    </tr>
                                 </tbody>
                              </table>
                           </td>
                        </tr>
                        <tr>
                           <td height="35" class="em_height" style="height: 35px"></td>
                        </tr>
                        <tr>
                           <td style="">
                              <table border="0" cellpadding="0" cellspacing="0" width="100%" >
                                 <tbody>
                                    <tr>
                                       <td>
                                          <table border="0" cellpadding="0" cellspacing="0" width="100%" >
                                             <tbody>
                                                <tr>
                                                   <td align="left" valign="top">
                                                      <table border="0" cellpadding="0" cellspacing="0" width="100%" >
                                                         <tbody>
                                                            <tr>
                                                               <td style="font-size: 16px; font-family: Lato, Helvetica, Helvetica, Arial, sans-serif; line-height: 1.5; letter-spacing: 0.3px; font-weight: bold; color: #202E3A; color: {{ header_text_color }};" > <b>{{ shipping_address_text }}</b> </td>
                                                            </tr>
                                                            <tr>
                                                               <td height="5" style="height: 5" ></td>
                                                            </tr>
                                                            <tr>
                                                               <td style="font-size: 14px; font-family: Lato, Helvetica, Arial, sans-serif; line-height: 1.5; letter-spacing: 0.3px; color: #495661; color: {{ text_color }};" > {{shippingAddress | newline_to_br}} </td>
                                                            </tr>
                                                         </tbody>
                                                      </table>
                                                   </td>
                                                   <td width="35" style="width: 35px"></td>
                                                   <td align="right" valign="top">
                                                      <table border="0" cellpadding="0" cellspacing="0" width="100%" align="right" valign="top" >
                                                         <tbody>
                                                            <tr>
                                                               <td style="font-size: 16px; font-family: Lato, Helvetica, Helvetica, Arial, sans-serif; line-height: 1.5; letter-spacing: 0.3px; font-weight: bold; color: #202E3A; color: {{ header_text_color }};" align="right" > <b>{{ billing_address_text }}</b> </td>
                                                            </tr>
                                                            <tr>
                                                               <td height="5" style="height: 5px" ></td>
                                                            </tr>
                                                            <tr>
                                                               <td align="right" valign="top" style="font-size: 14px; font-family: Lato, Helvetica, Arial, sans-serif; line-height: 1.5; letter-spacing: 0.3px; color: #495661; color: {{ text_color }};" > {{billingAddress | newline_to_br}} </td>
                                                            </tr>
                                                         </tbody>
                                                      </table>
                                                   </td>
                                                </tr>
                                                <tr>
                                                   <td height="20"></td>
                                                </tr>
                                                <tr>
                                                   <td align="left" valign="top">
                                                      <table border="0" cellpadding="0" cellspacing="0" width="100%" >
                                                         <tbody>
                                                            <tr>
                                                               <td style="font-size: 16px; font-family: Lato, Helvetica, Arial, sans-serif; line-height: 1.5; letter-spacing: 0.3px; font-weight: bold; color: #202E3A; color: {{ header_text_color }};" > <b>{{ next_orderdate_text }}</b> </td>
                                                            </tr>
                                                            <tr>
                                                               <td height="5" style="height: 5px" ></td>
                                                            </tr>
                                                            <tr>
                                                               <td style="font-size: 14px; font-family: Lato, Helvetica, Arial, sans-serif; line-height: 1.5; letter-spacing: 0.3px; color: #495661; color: {{ text_color }};" > {{ nextOrderDate }} </td>
                                                            </tr>
                                                         </tbody>
                                                      </table>
                                                   </td>
                                                   <td width="36" style="width: 35px"></td>
                                                   <td align="right" valign="top">
                                                      <table border="0" cellpadding="0" cellspacing="0" width="100%" align="right" valign="top" >
                                                         <tbody>
                                                            <tr>
                                                               <td style="font-size: 16px; font-family: Lato, Helvetica, Arial, sans-serif; line-height: 1.5; letter-spacing: 0.3px; font-weight: bold; color: #202E3A; color: {{ header_text_color }};" align="right" > <b>{{ payment_method_text }}</b> </td>
                                                            </tr>
                                                            <tr>
                                                               <td height="5" style="height: 5px" ></td>
                                                            </tr>
                                                            <tr>
                                                               <td align="right" valign="top">
                                                                  <table>
                                                                     <tbody border="0" cellpadding="0" cellspacing="0" width="100%" >
                                                                        <tr>
                                                                           <td> <img width="30" src="{{ cardLogo }}" /> </td>
                                                                           <td width="5" style="width: 5px" ></td>
                                                                           <td style="font-size: 14px; font-family: Lato, Helvetica, Arial, sans-serif; line-height: 1.5; letter-spacing: 0.3px; color: #495661; color: {{ text_color }};" > {{ maskedCardNumber }} </td>
                                                                        </tr>
                                                                     </tbody>
                                                                  </table>
                                                               </td>
                                                            </tr>
                                                         </tbody>
                                                      </table>
                                                   </td>
                                                </tr>
                                             </tbody>
                                          </table>
                                       </td>
                                    </tr>
                                 </tbody>
                              </table>
                           </td>
                        </tr>
                        <tr>
                           <td height="35" class="em_height" style="height: 35px"></td>
                        </tr>
                        <tr>
                           <td>
                              <table border="0" cellpadding="0" cellspacing="0" width="100%" style="border-collapse: collapse" >
                                 <tbody>
                                    <tr>
                                       <td style="">
                                          <table>
                                             <tbody>
                                                <tr>
                                                   <td height="1" bgcolor="#cccccc" style="font-size: 1px; line-height: 1px" ></td>
                                                </tr>
                                                <tr>
                                                   <td height="15" style="height: 15px"></td>
                                                </tr>
                                                <tr>
                                                   <td style="font-size: 14px; font-family: Lato, Helvetica, Arial, sans-serif; line-height: 1.5; letter-spacing: 0.3px; color: #495661; color: {{ footer_text_color }};" > {{ footer | newline_to_br }} </td>
                                                </tr>
                                                <tr>
                                                   <td height="15" style="height: 15px"></td>
                                                </tr>
                                             </tbody>
                                          </table>
                                       </td>
                                    </tr>
                                 </tbody>
                              </table>
                           </td>
                        </tr>
                     </tbody>
                  </table>
               </td>
            </tr>
         </tbody>
      </table>
   </body>
</html>
`;

export default defaultHTMLContent;