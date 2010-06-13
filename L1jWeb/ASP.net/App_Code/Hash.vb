Imports System
Imports System.Security.Cryptography
Imports System.Text

Namespace Hash
    ''' <summary>Class used to generate and check hashes.</summary>
    Public Class Hash
        ''' <summary></summary>
        Public Sub New()
        End Sub

#Region "Hash Choices"
        ''' <summary>The wanted hash function.</summary>
        Public Enum HashType As Integer
            ''' <summary>MD5 Hashing</summary>
            MD5
            ''' <summary>SHA1 Hashing</summary>
            SHA1
            ''' <summary>SHA256 Hashing</summary>
            SHA256
            ''' <summary>SHA384 Hashing</summary>
            SHA384
            ''' <summary>SHA512 Hashing</summary>
            SHA512
        End Enum
        ' HashType 
#End Region

#Region "Public Methods"
        ''' <summary>Generates the hash of a text.</summary>
        ''' <param name="strPlain">The text of which to generate a hash of.</param>
        ''' <param name="hshType">The hash function to use.</param>
        ''' <returns>The hash as a hexadecimal string.</returns>
        Public Shared Function GetHash(ByVal strPlain As String, ByVal hshType As HashType) As String
            Dim strRet As String
            Select Case hshType
                Case HashType.MD5
                    strRet = GetMD5(strPlain)
                    Exit Select
                Case HashType.SHA1
                    strRet = GetSHA1(strPlain)
                    Exit Select
                Case HashType.SHA256
                    strRet = GetSHA256(strPlain)
                    Exit Select
                Case HashType.SHA384
                    strRet = GetSHA384(strPlain)
                    Exit Select
                Case HashType.SHA512
                    strRet = GetSHA512(strPlain)
                    Exit Select
                Case Else
                    strRet = "Invalid HashType"
                    Exit Select
            End Select
            Return strRet
        End Function
        ' GetHash 

        ''' <summary>Checks a text with a hash.</summary>
        ''' <param name="strOriginal">The text to compare the hash against.</param>
        ''' <param name="strHash">The hash to compare against.</param>
        ''' <param name="hshType">The type of hash.</param>
        ''' <returns>True if the hash validates, false otherwise.</returns>
        Public Shared Function CheckHash(ByVal strOriginal As String, ByVal strHash As String, ByVal hshType As HashType) As Boolean
            Dim strOrigHash As String = GetHash(strOriginal, hshType)
            Return (strOrigHash = strHash)
        End Function
        ' CheckHash 
#End Region

#Region "Hashers"
        Private Shared Function GetMD5(ByVal strPlain As String) As String
            'Dim UE As New UnicodeEncoding()
            Dim encoder As New UTF8Encoding()
            Dim HashValue As Byte(), MessageBytes As Byte() = encoder.GetBytes(strPlain)
            Dim md5 As MD5 = New MD5CryptoServiceProvider()
            Dim strHex As String = ""

            HashValue = md5.ComputeHash(MessageBytes)
            For Each b As Byte In HashValue
                strHex += [String].Format("{0:x2}", b)
            Next
            Return strHex
        End Function
        ' GetMD5 

        Private Shared Function GetSHA1(ByVal strPlain As String) As String
            'Dim UE As New UnicodeEncoding()
            Dim encoder As New UTF8Encoding()
            Dim HashValue As Byte(), MessageBytes As Byte() = encoder.GetBytes(strPlain)
            Dim SHhash As New SHA1Managed()
            Dim strHex As String = ""

            HashValue = SHhash.ComputeHash(MessageBytes)
            For Each b As Byte In HashValue
                strHex += [String].Format("{0:x2}", b)
            Next
            Return strHex
        End Function
        ' GetSHA1 

        Private Shared Function GetSHA256(ByVal strPlain As String) As String
            'Dim UE As New UnicodeEncoding()
            Dim encoder As New UTF8Encoding()
            Dim HashValue As Byte(), MessageBytes As Byte() = encoder.GetBytes(strPlain)
            Dim SHhash As New SHA256Managed()
            Dim strHex As String = ""

            HashValue = SHhash.ComputeHash(MessageBytes)
            For Each b As Byte In HashValue
                strHex += [String].Format("{0:x2}", b)
            Next
            Return strHex
        End Function
        ' GetSHA256 

        Private Shared Function GetSHA384(ByVal strPlain As String) As String
            'Dim UE As New UnicodeEncoding()
            Dim encoder As New UTF8Encoding()
            Dim HashValue As Byte(), MessageBytes As Byte() = encoder.GetBytes(strPlain)
            Dim SHhash As New SHA384Managed()
            Dim strHex As String = ""

            HashValue = SHhash.ComputeHash(MessageBytes)
            For Each b As Byte In HashValue
                strHex += [String].Format("{0:x2}", b)
            Next
            Return strHex
        End Function
        ' GetSHA384 

        Private Shared Function GetSHA512(ByVal strPlain As String) As String
            'Dim UE As New UnicodeEncoding()
            Dim encoder As New UTF8Encoding()
            Dim HashValue As Byte(), MessageBytes As Byte() = encoder.GetBytes(strPlain)
            Dim SHhash As New SHA512Managed()
            Dim strHex As String = ""

            HashValue = SHhash.ComputeHash(MessageBytes)
            For Each b As Byte In HashValue
                strHex += [String].Format("{0:x2}", b)
            Next
            Return strHex
        End Function
        ' GetSHA512 
#End Region

    End Class

End Namespace
