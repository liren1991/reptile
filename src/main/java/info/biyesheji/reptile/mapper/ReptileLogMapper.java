package info.biyesheji.reptile.mapper;

import info.biyesheji.reptile.entity.ReptileLog;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Options;

import java.util.List;

/**
 * ReptileLogDao.java
 * @version 1.0.0
 */
public interface ReptileLogMapper {

    String fields = " trl.id id, trl.url url, trl.language_type languageType, trl.start_num startNum, trl.remark remark, trl.type type, trl.status status, trl.project_name projectName, trl.git_url gitUrl ";

    /**
     * 根据ID查询 ReptileLog 实体
     */
    @Select(" select " + fields + " from t_reptile_log trl where trl.id = #{id}")
    ReptileLog getReptileLogByPrimaryId(@Param(value = "id") Integer id);

    /**
     * 根据ID修改 ReptileLog 实体
     */
    @Update("<script> update t_reptile_log" +
        "<set>" +
            "<if test='url !=null '>url = #{url},</if><if test='languageType !=null '>language_type = #{languageType},</if><if test='startNum !=null '>start_num = #{startNum},</if><if test='remark !=null '>remark = #{remark},</if><if test='type !=null '>type = #{type},</if><if test='status !=null '>status = #{status},</if><if test='projectName !=null '>project_name = #{projectName},</if><if test='gitUrl !=null '>git_url = #{gitUrl}</if>" +
        "</set>" +
        "where id = #{id} </script>")
    Integer updateReptileLogByPrimaryId(ReptileLog ReptileLog);

    /**
     * 根据ID删除 ReptileLog 实体
     */
    @Delete("delete from t_reptile_log where id = #{id}")
    Integer delReptileLogByPrimaryId(@Param(value = "id") Integer id);

    /**
     * 保存 ReptileLog 实体
     */
    @Insert("<script> insert into t_reptile_log " +
        "<trim prefix=' ( ' suffix=' ) ' suffixOverrides=' , '>" +
            "<if test='id !=null '>id,</if><if test='url !=null '>url,</if><if test='languageType !=null '>language_type,</if><if test='startNum !=null '>start_num,</if><if test='remark !=null '>remark,</if><if test='type !=null '>type,</if><if test='status !=null '>status,</if><if test='projectName !=null '>project_name,</if><if test='gitUrl !=null '>git_url</if>" +
        "</trim>" +
        "<trim prefix=' values( ' suffix=' ) ' suffixOverrides=','><if test='id !=null '>#{id},</if><if test='url !=null '>#{url},</if><if test='languageType !=null '>#{languageType},</if><if test='startNum !=null '>#{startNum},</if><if test='remark !=null '>#{remark},</if><if test='type !=null '>#{type},</if><if test='status !=null '>#{status},</if><if test='projectName !=null '>#{projectName},</if><if test='gitUrl !=null '>#{gitUrl}</if>"+
        "</trim></script>")
    @Options(useGeneratedKeys=true,keyProperty = "id")
    Integer addReptileLog(ReptileLog ReptileLog);

    @Select("select " + fields + " from t_reptile_log trl where trl.status = #{status}")
    List<ReptileLog> listReptileLogTask(@Param("status") Integer status);
}