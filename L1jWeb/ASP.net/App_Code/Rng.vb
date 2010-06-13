Imports System
Imports System.Security.Cryptography
Imports Microsoft.VisualBasic

''' <summary>
''' 使用 RNGCryptoServiceProvider 產生由密碼編譯服務供應者 (CSP) 提供的亂數產生器。
''' </summary>
Public Class RNG

    Private rngp As New RNGCryptoServiceProvider()
    Private rb As Byte() = New Byte(3) {}

    Public Sub New()

    End Sub

    ''' <summary>
    ''' 產生一個非負數的亂數
    ''' </summary>
    Public Function [Next]() As Integer
        rngp.GetBytes(rb)
        Dim value As Integer = BitConverter.ToInt32(rb, 0)
        If value < 0 Then
            value = -value
        End If
        Return value
    End Function

    ''' <summary>
    ''' 產生一個非負數且最大值 max 以下的亂數
    ''' </summary>
    ''' <param name="max">最大值</param>
    Public Function [Next](ByVal max As Integer) As Integer
        rngp.GetBytes(rb)
        Dim value As Integer = BitConverter.ToInt32(rb, 0)
        value = value Mod (max + 1)
        If value < 0 Then
            value = -value
        End If
        Return value
    End Function

    ''' <summary>
    ''' 產生一個非負數且最小值在 min 以上最大值在 max 以下的亂數
    ''' </summary>
    ''' <param name="min">最小值</param>
    ''' <param name="max">最大值</param>
    Public Function [Next](ByVal min As Integer, ByVal max As Integer) As Integer
        Dim value As Integer = [Next](max - min) + min
        Return value
    End Function

End Class