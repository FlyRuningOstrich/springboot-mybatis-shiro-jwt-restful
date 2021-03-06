package server.db.primary.model.sheet;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "REPORT_DATA_OWATER_LOOP")
@Accessors(chain = true)
public class SimpleSheet {
    private Long id;
    private String report_hour;//report_hour  时间
    private String report_loop_name;//report_loop_name  掺水阀组
    private Double temp_out;//temp_out  出站温度(℃)
    private Double press_out;//press_out  出站压力（MPa）
    private Double flow_inst_out;//flow_inst_out  瞬时流量（m³/h）
    private Double flow_totle_out;//flow_totle_out  流量计读数
    private Double water_val;//water_val  水量(m³)
    private Double temp_in;//temp_in  入站温度(℃)
    private Double press_in;//press_in  入站压力（MPa）
    private Double flow_inst_in;//flow_inst_in  瞬时流量（m³/h）
    private Double flow_totle_in;//flow_totle_in  流量计读数
    private Double liquid_val;//liquid_val  液量(m³)
    private String reportDate;
    private Long reportLoopId;
    private Long reportStationId;
    private String reportStationName;
    private Long areaId;
    private String areaName;
    private String remark;
    private Short mark;
    private Date stime;
}