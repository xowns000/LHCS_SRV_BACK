package kr.co.hkcloud.palette3.admin.custco.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * IPCC API 호출 오류 시 데이터를 롤백하기 위한 정보 
 * @author hjh
 *
 */
@Getter
@Setter
public class IpccRollbackDTO
{
	private String apiType;
	private String req;
	private String custCode;
	private String cName;
	private String compTel;
	private String compman;
	private String compmanTel;
	private String compState;
	private String did;
	private String cidName;
	private String trkName;
	
	public void setCustReq()
	{
		
	}
	
	public void setTrunkReq()
	{
		
	}
}
