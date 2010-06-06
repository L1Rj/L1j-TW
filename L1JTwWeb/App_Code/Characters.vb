Imports Microsoft.VisualBasic
Imports MySql.Data.MySqlClient
Imports System.Data

Public Class Characters

    Private _db As SQLMethod

    Private _account_name As String
    Private _objid As Integer
    Private _char_name As String
    Private _level As Integer
    Private _highlevel As Integer
    Private _exp As Integer
    Private _maxhp As Integer
    Private _curhp As Integer
    Private _maxmp As Integer
    Private _curmp As Integer
    Private _ac As Integer
    Private _lucky As Integer
    Private _str As Integer
    Private _con As Integer
    Private _dex As Integer
    Private _cha As Integer
    Private _intel As Integer
    Private _wis As Integer
    Private _status As Integer
    Private _class As Integer
    Private _sex As Integer
    Private _type As Integer
    Private _heading As Integer
    Private _locx As Integer
    Private _locy As Integer
    Private _mapid As Integer
    Private _food As Integer
    Private _lawful As Integer
    Private _title As String
    Private _clanid As Integer      '盟主ID
    Private _clanname As String     '血盟名稱
    Private _clanrank As Integer
    Private _pkcount As Integer
    Private _onlinestatus As Integer
    Private _banned As Integer
    Private _OriginalStr As Integer
    Private _OriginalCon As Integer
    Private _OriginalDex As Integer
    Private _OriginalCha As Integer
    Private _OriginalInt As Integer
    Private _OriginalWis As Integer

    Private strErrMsg As String

    Public Sub New()

    End Sub

#Region "characters property"
    Public Property Account_Name() As String
        Get
            Return _account_name
        End Get
        Set(ByVal value As String)
            _account_name = value
        End Set
    End Property

    Public Property ObjID() As String
        Get
            Return _objid
        End Get
        Set(ByVal value As String)
            _objid = value
        End Set
    End Property

    Public Property Char_Name() As String
        Get
            Return _char_name
        End Get
        Set(ByVal value As String)
            _char_name = value
        End Set
    End Property
    Public Property Level() As String
        Get
            Return _level
        End Get
        Set(ByVal value As String)
            _level = value
        End Set
    End Property
    Public Property HighLevel() As String
        Get
            Return _highlevel
        End Get
        Set(ByVal value As String)
            _highlevel = value
        End Set
    End Property
    Public Property Exp() As String
        Get
            Return _exp
        End Get
        Set(ByVal value As String)
            _exp = value
        End Set
    End Property
    Public Property MaxHp() As String
        Get
            Return _maxhp
        End Get
        Set(ByVal value As String)
            _maxhp = value
        End Set
    End Property
    Public Property CurHp() As String
        Get
            Return _curhp
        End Get
        Set(ByVal value As String)
            _curhp = value
        End Set
    End Property
    Public Property MaxMp() As String
        Get
            Return _maxmp
        End Get
        Set(ByVal value As String)
            _maxmp = value
        End Set
    End Property
    Public Property CurMp() As String
        Get
            Return _curmp
        End Get
        Set(ByVal value As String)
            _curmp = value
        End Set
    End Property
    Public Property Ac() As String
        Get
            Return _ac
        End Get
        Set(ByVal value As String)
            _ac = value
        End Set
    End Property
    Public Property Lucky() As String
        Get
            Return _lucky
        End Get
        Set(ByVal value As String)
            _lucky = value
        End Set
    End Property
    Public Property Str() As String
        Get
            Return _str
        End Get
        Set(ByVal value As String)
            _str = value
        End Set
    End Property
    Public Property Con() As String
        Get
            Return _con
        End Get
        Set(ByVal value As String)
            _con = value
        End Set
    End Property
    Public Property Dex() As String
        Get
            Return _dex
        End Get
        Set(ByVal value As String)
            _dex = value
        End Set
    End Property
    Public Property Cha() As String
        Get
            Return _cha
        End Get
        Set(ByVal value As String)
            _cha = value
        End Set
    End Property
    Public Property Intel() As String
        Get
            Return _intel
        End Get
        Set(ByVal value As String)
            _intel = value
        End Set
    End Property
    Public Property Wis() As String
        Get
            Return _wis
        End Get
        Set(ByVal value As String)
            _wis = value
        End Set
    End Property
    Public Property Status() As String
        Get
            Return _status
        End Get
        Set(ByVal value As String)
            _status = value
        End Set
    End Property
    Public Property GetClass() As String
        Get
            Return _class
        End Get
        Set(ByVal value As String)
            _class = value
        End Set
    End Property
    Public Property Sex() As String
        Get
            Return _sex
        End Get
        Set(ByVal value As String)
            _sex = value
        End Set
    End Property
    Public Property Type() As String
        Get
            Return _type
        End Get
        Set(ByVal value As String)
            _type = value
        End Set
    End Property
    Public Property Heading() As String
        Get
            Return _heading
        End Get
        Set(ByVal value As String)
            _heading = value
        End Set
    End Property
    Public Property LocX() As String
        Get
            Return _locx
        End Get
        Set(ByVal value As String)
            _locx = value
        End Set
    End Property
    Public Property LocY() As String
        Get
            Return _locy
        End Get
        Set(ByVal value As String)
            _locy = value
        End Set
    End Property
    Public Property MapID() As String
        Get
            Return _mapid
        End Get
        Set(ByVal value As String)
            _mapid = value
        End Set
    End Property
    Public Property Food() As String
        Get
            Return _food
        End Get
        Set(ByVal value As String)
            _food = value
        End Set
    End Property
    Public Property Lawful() As String
        Get
            Return _lawful
        End Get
        Set(ByVal value As String)
            _lawful = value
        End Set
    End Property
    Public Property Title() As String
        Get
            Return _title
        End Get
        Set(ByVal value As String)
            _title = value
        End Set
    End Property
    Public Property ClanID() As String
        Get
            Return _clanid
        End Get
        Set(ByVal value As String)
            _clanid = value
        End Set
    End Property
    Public Property ClanName() As String
        Get
            Return _clanname
        End Get
        Set(ByVal value As String)
            _clanname = value
        End Set
    End Property
    Public Property ClanRank() As String
        Get
            Return _clanrank
        End Get
        Set(ByVal value As String)
            _clanrank = value
        End Set
    End Property
    Public Property PKcount() As String
        Get
            Return _pkcount
        End Get
        Set(ByVal value As String)
            _pkcount = value
        End Set
    End Property
    Public Property OnlineStatus() As String
        Get
            Return _onlinestatus
        End Get
        Set(ByVal value As String)
            _onlinestatus = value
        End Set
    End Property
    Public Property Banned() As String
        Get
            Return _banned
        End Get
        Set(ByVal value As String)
            _banned = value
        End Set
    End Property
    Public Property OriginalStr() As String
        Get
            Return _OriginalStr
        End Get
        Set(ByVal value As String)
            _OriginalStr = value
        End Set
    End Property
    Public Property OriginalCon() As String
        Get
            Return _OriginalCon
        End Get
        Set(ByVal value As String)
            _OriginalCon = value
        End Set
    End Property
    Public Property OriginalDex() As String
        Get
            Return _OriginalDex
        End Get
        Set(ByVal value As String)
            _OriginalDex = value
        End Set
    End Property
    Public Property OriginalCha() As String
        Get
            Return _OriginalCha
        End Get
        Set(ByVal value As String)
            _OriginalCha = value
        End Set
    End Property
    Public Property OriginalInt() As String
        Get
            Return _OriginalInt
        End Get
        Set(ByVal value As String)
            _OriginalInt = value
        End Set
    End Property
    Public Property OriginalWis() As String
        Get
            Return _OriginalWis
        End Get
        Set(ByVal value As String)
            _OriginalWis = value
        End Set
    End Property
#End Region

#Region "characters list"
    Public Function GetCharacterInfo(ByVal GameAccount As String) As List(Of Characters)
        Dim tmpCharacters As Characters
        _db = New SQLMethod
        Dim strSQL As String = "SELECT * FROM characters WHERE account_name=@account_name "
        Dim cmd As New MySqlCommand()
        cmd.CommandText = strSQL
        cmd.Parameters.Add("@account_name", MySqlDbType.VarChar).Value = GameAccount
        Dim tmpDT As DataTable = _db.GetDataTable(cmd, "characters")

        Dim _list_character As New List(Of Characters)

        For i As Integer = 0 To tmpDT.Rows.Count - 1
            tmpCharacters = New Characters
            tmpCharacters.Ac = CInt(tmpDT.Rows(i)("ac"))
            tmpCharacters.Account_Name = tmpDT.Rows(i)("account_name").ToString()
            tmpCharacters.Banned = CInt(tmpDT.Rows(i)("Banned"))
            tmpCharacters.Cha = CInt(tmpDT.Rows(i)("Cha"))
            tmpCharacters.Char_Name = tmpDT.Rows(i)("char_name").ToString()
            tmpCharacters.ClanID = CInt(tmpDT.Rows(i)("ClanID"))
            tmpCharacters.ClanName = tmpDT.Rows(i)("ClanName").ToString()
            tmpCharacters.ClanRank = CInt(tmpDT.Rows(i)("ClanRank"))
            tmpCharacters.Con = CInt(tmpDT.Rows(i)("con"))
            tmpCharacters.CurHp = CInt(tmpDT.Rows(i)("CurHp"))
            tmpCharacters.CurMp = CInt(tmpDT.Rows(i)("CurMp"))
            tmpCharacters.Dex = CInt(tmpDT.Rows(i)("Dex"))
            tmpCharacters.Exp = CInt(tmpDT.Rows(i)("Exp"))
            tmpCharacters.Food = CInt(tmpDT.Rows(i)("Food"))
            tmpCharacters.GetClass = CInt(tmpDT.Rows(i)("Class"))
            tmpCharacters.Heading = CInt(tmpDT.Rows(i)("Heading"))
            tmpCharacters.HighLevel = CInt(tmpDT.Rows(i)("HighLevel"))
            tmpCharacters.Intel = CInt(tmpDT.Rows(i)("Intel"))
            tmpCharacters.Lawful = CInt(tmpDT.Rows(i)("Lawful"))
            tmpCharacters.Level = CInt(tmpDT.Rows(i)("Level"))
            tmpCharacters.LocX = CInt(tmpDT.Rows(i)("LocX"))
            tmpCharacters.LocY = CInt(tmpDT.Rows(i)("LocY"))
            tmpCharacters.Lucky = CInt(tmpDT.Rows(i)("Lucky"))
            tmpCharacters.MapID = CInt(tmpDT.Rows(i)("MapID"))
            tmpCharacters.MaxHp = CInt(tmpDT.Rows(i)("MaxHp"))
            tmpCharacters.MaxMp = CInt(tmpDT.Rows(i)("MaxMp"))
            tmpCharacters.ObjID = CInt(tmpDT.Rows(i)("objid"))
            tmpCharacters.OnlineStatus = CInt(tmpDT.Rows(i)("OnlineStatus"))
            tmpCharacters.OriginalCha = CInt(tmpDT.Rows(i)("OriginalCha"))
            tmpCharacters.OriginalCon = CInt(tmpDT.Rows(i)("OriginalCon"))
            tmpCharacters.OriginalDex = CInt(tmpDT.Rows(i)("OriginalDex"))
            tmpCharacters.OriginalInt = CInt(tmpDT.Rows(i)("OriginalInt"))
            tmpCharacters.OriginalStr = CInt(tmpDT.Rows(i)("OriginalStr"))
            tmpCharacters.OriginalWis = CInt(tmpDT.Rows(i)("OriginalWis"))
            tmpCharacters.PKcount = CInt(tmpDT.Rows(i)("PKcount"))
            tmpCharacters.Sex = CInt(tmpDT.Rows(i)("Sex"))
            tmpCharacters.Status = CInt(tmpDT.Rows(i)("Status"))
            tmpCharacters.Str = CInt(tmpDT.Rows(i)("Str"))
            tmpCharacters.Title = tmpDT.Rows(i)("Title").ToString()
            tmpCharacters.Type = CInt(tmpDT.Rows(i)("Type"))
            tmpCharacters.Wis = CInt(tmpDT.Rows(i)("Wis"))

            _list_character.Add(tmpCharacters)
        Next

        Return _list_character
    End Function
#End Region

#Region "characters datatable"
    Public Function GetCharacterDT(ByVal GameAccount As String) As DataTable
        _db = New SQLMethod
        Dim strSQL As String = "SELECT * FROM characters WHERE account_name=@account_name "
        Dim cmd As New MySqlCommand()
        cmd.CommandText = strSQL
        cmd.Parameters.Add("@account_name", MySqlDbType.VarChar).Value = GameAccount
        Dim tmpDT As DataTable = _db.GetDataTable(cmd, "characters")

        Return tmpDT
    End Function
#End Region

End Class
