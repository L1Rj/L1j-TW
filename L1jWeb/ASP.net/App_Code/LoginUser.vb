Imports Microsoft.VisualBasic

Public Class LoginUser

    'web_accounts table
    Private _account As String
    Private _access_level As Integer
    Private _ip As String
    Private _host As String
    Private _banned As Integer
    'Private _character_slot As Integer

    'personal table
    Private _email As String

    Private strErrMsg As String

    '建構式
    Public Sub New()

    End Sub

    Public Property Account() As String
        Get
            Return _account
        End Get
        Set(ByVal value As String)
            _account = value
        End Set
    End Property

    Public Property Access_Level() As Integer
        Get
            Return _access_level
        End Get
        Set(ByVal value As Integer)
            _access_level = value
        End Set
    End Property

    Public Property IP() As String
        Get
            Return _ip
        End Get
        Set(ByVal value As String)
            _ip = value
        End Set
    End Property

    Public Property Host() As String
        Get
            Return _host
        End Get
        Set(ByVal value As String)
            _host = value
        End Set
    End Property

    Public Property Banned() As Integer
        Get
            Return _banned
        End Get
        Set(ByVal value As Integer)
            _banned = value
        End Set
    End Property

    'Public Property Character_Slot() As Integer
    '    Get
    '        Return _character_slot
    '    End Get
    '    Set(ByVal value As Integer)
    '        _character_slot = value
    '    End Set
    'End Property

    Public Property Email() As String
        Get
            Return _email
        End Get
        Set(ByVal value As String)
            _email = value
        End Set
    End Property

    Public Property ErrMsg() As String
        Get
            Return strErrMsg
        End Get
        Set(ByVal value As String)
            strErrMsg = value
        End Set
    End Property

End Class
