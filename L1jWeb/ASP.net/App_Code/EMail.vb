Imports System.IO
Imports System.Net.Mail
Imports System.Collections.Generic
Imports Microsoft.VisualBasic

Public Class EMail
    Private _from As MailAddress       '寄件者  
    Private _to As List(Of MailAddress)  '收件者  
    Private _cc As List(Of MailAddress)   '副本  
    Private _bcc As List(Of MailAddress)  '密件副本  

    Private _attachment As List(Of FileInfo) '附加檔  
    Private _body As String = ""       '信件內容  
    Private _subject As String = ""    '主旨  
    Private _isBodyHtml As Boolean = True  '內容格式是否HTML  

    '寄件者  
    Public Property FromMail() As MailAddress
        Get
            Return _from
        End Get
        Set(ByVal value As MailAddress)
            _from = value
        End Set
    End Property

    '收件者  
    Public Property ToMail() As List(Of MailAddress)
        Get
            Return _to
        End Get
        Set(ByVal value As List(Of MailAddress))
            _to = value
        End Set
    End Property

    '副本  
    Public Property cc() As List(Of MailAddress)
        Get
            Return _cc
        End Get
        Set(ByVal value As List(Of MailAddress))
            _cc = value
        End Set
    End Property

    '密件副本  
    Public Property bcc() As List(Of MailAddress)
        Get
            Return _bcc
        End Get
        Set(ByVal value As List(Of MailAddress))
            _bcc = value
        End Set
    End Property

    '附加檔  
    Public Property Attachment() As List(Of FileInfo)
        Get
            Return _attachment
        End Get
        Set(ByVal value As List(Of FileInfo))
            _attachment = value
        End Set
    End Property

    '內容  
    Public Property Body() As String
        Get
            Return _body
        End Get
        Set(ByVal value As String)
            _body = value
        End Set
    End Property

    '主旨  
    Public Property Subject() As String
        Get
            Return _subject
        End Get
        Set(ByVal value As String)
            _subject = value
        End Set
    End Property

    '內容格式是否HTML  
    Public Property isBodyHtml() As Boolean
        Get
            Return _isBodyHtml
        End Get
        Set(ByVal value As Boolean)
            _isBodyHtml = value
        End Set
    End Property

    Private Function ChkData() As String
        Try
            Dim Rc As String = ""
            If _from Is Nothing Then
                Rc += "無寄件者"
            End If
            If _to Is Nothing And _cc Is Nothing And _bcc Is Nothing Then
                Rc += "無收件者"
            End If
            If _subject Is Nothing Then
                Rc += "無主旨"
            Else
                If _subject.Trim() = "" Then
                    Rc += "無主指"
                End If
            End If
            If _body Is Nothing Then
                Rc += "無內容"
            Else
                If _body.Trim() = "" Then
                    Rc += "無內容"
                End If
            End If
            If Rc = "" Then
                Rc = "Success"
            End If
            Return Rc

        Catch ex As Exception
            Throw
        End Try
    End Function

    Public Function SendMail() As String
        Try
            '檢查必要項目  
            Dim chkRc As String = ChkData()
            If chkRc <> "Success" Then
                Throw New Exception(chkRc)
            Else
                Dim MMsg As New MailMessage()   '宣告並實體化MailMessage     
                Dim y As Integer                '宣告整數     

                '宣告並實體化SmtpClient，設定MailServer,Port
                Dim _smtpserver As String = ConfigurationManager.AppSettings.Get("SmtpServer")
                Dim smtpClnt As New SmtpClient(_smtpserver, "25") '請更改為自己的MailServer     

                MMsg.From = _from  '設定寄件者     
                MMsg.Subject = _subject  '設定主旨     
                MMsg.IsBodyHtml = _isBodyHtml    '設定內容是否為HTML格式     
                MMsg.SubjectEncoding = Encoding.UTF8    '主旨指定用UTF-8格式
                MMsg.BodyEncoding = Encoding.UTF8   '內容指定用UTF-8格式

                Dim tBody As String
                If isBodyHtml Then  '如果是HTML格式     
                    'Body = Replace(Body, vbCrLf, "")    '取代換行     
                    '信件內容控制     
                    tBody = ".dl{font-size:14}" + _body + ""
                Else    '非HTML格式     
                    tBody = _body
                End If
                '設定Message信件內容     
                MMsg.Body = tBody

                '處理收件者  
                If _to IsNot Nothing Then
                    If _to.Count > 0 Then
                        For y = 0 To _to.Count - 1
                            MMsg.To.Add(_to.Item(y))
                        Next
                    End If
                End If

                '處理副本  
                If _cc IsNot Nothing Then
                    If _cc.Count > 0 Then
                        For y = 0 To _cc.Count - 1
                            MMsg.CC.Add(_cc.Item(y))
                        Next
                    End If
                End If

                '處理密件副本  
                If _bcc IsNot Nothing Then
                    If _bcc.Count > 0 Then
                        For y = 0 To _bcc.Count - 1
                            MMsg.Bcc.Add(_bcc.Item(y))
                        Next
                    End If
                End If

                '處理附加檔  
                If _attachment IsNot Nothing Then
                    If _attachment.Count > 0 Then
                        Dim matt As Attachment
                        For y = 0 To _attachment.Count - 1
                            matt = New Attachment(_attachment.Item(y).FullName)
                            MMsg.Attachments.Add(matt)
                        Next
                    End If
                End If

                '送出郵件  
                smtpClnt.Send(MMsg)
                Return "Success"
            End If
        Catch ex As Exception
            Throw New Exception(ex.Message)
        End Try
    End Function

End Class