package com.MyTime.service;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.springframework.util.ResourceUtils;


import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.sql.DataSource;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Service
public class CsvService {

    @Autowired
    EntityManager entityManager;

    @Autowired
    protected DataSource localDataSource;
    @Transactional(readOnly = true)
    public byte[] createCsvFile(long companyId, long year, long month) throws IOException {

        String queryString = "";
        queryString = queryString.concat(" SELECT ")
                .concat(" u.name as username,u.status as userstatus, ")
                .concat(" p.name as projectname, p.start_date as startdate,p.end_date as enddate,")
                .concat(" pt.name_task as taskname,pt.code_task as taskcode,")
                .concat(" ut.create_date as createdateweek, ut.modified_date as modifieddateweek, ")
                .concat(" ut.name as nameweek, ut.notes as notesweek,ut.status as status, ut.year as year, ut.month as month, ut.week as week, ")
                .concat(" itd.create_date as detailcreatedate, itd.modified_date as detailmodifieddate, itd.notes as notesday,")
                .concat(" itd.sat as sat, itd.sun as sun, itd.mon as mon, itd.tus as tus, ")
                .concat(" itd.wen as wen,itd.tru as tru, itd.fry as fry")
                .concat(" FROM user_time ut ")
                .concat(" inner join user_time_detail itd on ut.user_time_id = itd.user_time_id ")
                .concat(" inner join user u on ut.user_id = u.user_id ")
                .concat(" inner join project_task pt on pt.task_id = itd.task_id ")
                .concat(" inner join project p on pt.project_id = p.project_id ")
                .concat(" where itd.project_id in (")
                .concat(" select p.project_id as projectid from project pi where pi.company_id =:pcompanyId )")
                .concat(" and ut.year =:pyear")
                .concat(" and ut.month =:pmonth")
                .concat(" ");

        Query query = entityManager.createNativeQuery(queryString);

        query.setParameter("pcompanyId", companyId);
        query.setParameter("pyear", year);
        query.setParameter("pmonth", month);

        List<Object[]> resultList = query.getResultList();

        // Convertir los resultados a formato CSV
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        String headerRow = "USERNAME,USERSTATUS,PROJECTNAME,STARTDATE,ENDDATE,TASKNAME,TASKCODE,CREATEDATEWEEK,MODIFIEDDATEWEEK," +
                "NAMEWEEK,NOTESWEEK,STATUS,YEAR,MONTH,WEEK,DETAILCREATEDATE,DETAILMODIFIEDDATE,NOTESDAY," +
                "SAT,SUN,MON,TUS,WEN,TRU,FRY" + System.lineSeparator();
        byteArrayOutputStream.write(headerRow.getBytes());
        for (Object[] resultado : resultList) {
            String csvRow = IntStream.range(0, resultado.length)
                    .mapToObj(i -> escapeCsvValue(resultado[i]))
                    .collect(Collectors.joining(",")) + System.lineSeparator();
            byteArrayOutputStream.write(csvRow.getBytes());
        }

        return byteArrayOutputStream.toByteArray();
    }

    private String escapeCsvValue(Object value) {
        if (value == null) {
            return "";
        }

        String stringValue = value.toString();
        if (stringValue.contains(",") || stringValue.contains("\"") || stringValue.contains("\n")) {
            // Si el valor contiene comas, comillas o saltos de línea, rodearlo con comillas dobles y duplicar las comillas dentro del valor
            return "\"" + stringValue.replace("\"", "\"\"") + "\"";
        } else {
            return stringValue;
        }
    }

    public byte[] createPDFFile(long companyId, long year, long month) throws IOException, JRException, SQLException {

        // Configurar el informe Jasper

        File jrxmlFile = ResourceUtils.getFile("classpath:reports/myTimeMonth.jrxml");

        System.out.println(jrxmlFile.getName());

        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlFile.getAbsolutePath());

        // Configurar los parámetros del informe
        Map<String, Object> params = new HashMap<>();
        params.put("P_COMPANYID", companyId);
        params.put("P_YEAR", year);
        params.put("P_MONTH", month);
        // Agregar parámetros si es necesario

        // Llenar el informe con datos
        java.sql.Connection con = localDataSource.getConnection();

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, con);

        // Exportar a PDF
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JRPdfExporter exporter = new JRPdfExporter();
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(baos));
        exporter.exportReport();

        return baos.toByteArray();
    }

}