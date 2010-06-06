Imports System.Data
Imports System.Data.SqlClient
Imports Microsoft.VisualBasic
Imports MySql.Data.MySqlClient

Public Class SQLMethod
    Dim objconn As ConnectionStringSettings = ConfigurationManager.ConnectionStrings("L1JDBConnectionString")
    'Dim conn As New Data.SqlClient.SqlConnection(objconn.ConnectionString)
    Dim conn As New MySqlConnection(objconn.ConnectionString)
    Private strErrMsg As String

    Public Property ErrMsg() As String
        Get
            Return strErrMsg
        End Get
        Set(ByVal value As String)
            strErrMsg = value
        End Set
    End Property

    Public Function GetFirstValue(ByVal cmd As MySqlCommand) As String
        strErrMsg = String.Empty
        Dim Result As String = String.Empty
        Try
            openConnection()
            cmd.Connection = conn
            Result = cmd.ExecuteScalar()
            'conn.Close()
        Catch ex As Exception
            strErrMsg = ex.Message()
        Finally
            cmd.Dispose()
            'conn.Close()
            'conn.Dispose()
        End Try

        Return Result
    End Function

    Public Function GetDataTable(ByVal cmd As MySqlCommand, ByVal TableName As String) As DataTable
        strErrMsg = String.Empty
        Dim tmpTable As New DataTable

        Try
            openConnection()
            cmd.Connection = conn
            Dim DA As New MySqlDataAdapter(cmd)
            DA.Fill(tmpTable)
            tmpTable.TableName = TableName
            DA.Dispose()
        Catch ex As Exception
            strErrMsg = ex.Message()
            tmpTable = Nothing
        Finally
            cmd.Dispose()
        End Try

        Return tmpTable
    End Function

    Public Function ExecSQL(ByVal cmd As MySqlCommand) As String
        strErrMsg = String.Empty

        Try
            openConnection()
            cmd.Connection = conn
            cmd.ExecuteNonQuery()
        Catch ex As SqlException
            strErrMsg = ex.Message()
        Finally
            cmd.Dispose()
        End Try

        Return strErrMsg
    End Function

    Public Function ExecSQL(ByVal CollCmd As List(Of MySqlCommand)) As String
        strErrMsg = String.Empty

        Try
            openConnection()
            Dim tran As MySqlTransaction = conn.BeginTransaction()
            Try

                For Each cmd As MySqlCommand In CollCmd
                    cmd.Connection = conn
                    cmd.Transaction = tran
                    cmd.ExecuteNonQuery()
                Next
                tran.Commit()
            Catch ex As MySqlException
                tran.Rollback()
                strErrMsg = ex.Message
            Catch ex As Exception
                tran.Rollback()
                strErrMsg = ex.Message()
            Finally
                'conn.Close()
            End Try
        Catch ex1 As Exception
            strErrMsg = ex1.Message
        End Try

        Return strErrMsg
    End Function

    Private Sub openConnection()
        If conn.State = Data.ConnectionState.Closed Then conn.Open()
    End Sub

End Class
