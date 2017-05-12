package BFC;
public class FileInfo{
	String filename;
	String file_id;
	String sha;
	String refrence_id;
	String start_chunk_id;
	int num_chunk;
	long file_size;
	int status;
	Object row[][];
public FileInfo(String filename,String file_id,String sha,String refrence_id,String start_chunk_id,int num_chunk,long file_size,int status,Object row[][]){
	this.filename = filename;
	this.file_id = file_id;
	this.sha = sha;
	this.refrence_id = refrence_id;
	this.start_chunk_id = start_chunk_id;
	this.num_chunk = num_chunk;
	this.file_size = file_size;
	this.status = status;
	this.row = row;
}
}