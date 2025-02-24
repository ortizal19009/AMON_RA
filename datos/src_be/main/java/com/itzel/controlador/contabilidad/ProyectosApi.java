package com.itzel.controlador.contabilidad;

import com.itzel.config.jasperConfig.ReportDTO;
import com.itzel.config.jasperConfig.ReportService;
import com.itzel.config.jasperConfig.Report_i;
import com.itzel.interfaces.contabilidad.Proyectos_rep_int;
import com.itzel.modelo.contabilidad.Proyectos;
import com.itzel.servicio.contabilidad.ProyectosService;
import net.sf.jasperreports.engine.JRException;
import org.springframework.core.io.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.InputStreamResource;


import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/proyectos")
@CrossOrigin("*")
public class ProyectosApi {
    private static final Logger logger = LoggerFactory.getLogger(ProyectosApi.class);

    @Autowired
    private ProyectosService proyectosService;
    @Autowired
    private ReportService jasperReportService;

   // public ProyectosApi(ReportService jasperReportService) {
     //   this.jasperReportService = jasperReportService;
    //}
    @Autowired
    private Report_i reportI;

    @GetMapping
    public ResponseEntity<List<Proyectos>> getAll(){
        List<Proyectos> proyectos  = proyectosService.findAll(Sort.by(Sort.Order.asc("codigo")));
        if (proyectos != null){
            return ResponseEntity.ok(proyectos);
        }
        return  ResponseEntity.noContent().build();
    }
    @PostMapping
    public ResponseEntity<Object> save(@RequestBody Proyectos e){
        return ResponseEntity.ok(proyectosService.save(e));
    }
    @PutMapping
    public ResponseEntity<Object> update(@RequestParam Long idproyecto, @RequestBody Proyectos e){
        Proyectos _e = proyectosService.findById(idproyecto).orElse(null);
        assert _e != null;
        _e.setCodigo(e.getCodigo());
        _e.setNombre(e.getNombre());
        _e.setMovimiento(e.getMovimiento());
        _e.setIdestructura(e.getIdestructura());
        _e.setUsumodi(e.getUsumodi());
        _e.setFecmodi(e.getFecmodi());
        return ResponseEntity.ok(proyectosService.update(_e));
    }
    @GetMapping("/validar/codigo")
    public ResponseEntity<Boolean> getByCodigo(@RequestParam String codigo){
        Proyectos proyectos = proyectosService.findByCodigo(codigo);
        return ResponseEntity.ok(proyectos != null);
    }
    @GetMapping("/validar/nombre")
    public ResponseEntity<Boolean> getByNombre(@RequestParam String nombre){
        Proyectos proyectos = proyectosService.findByNombre(nombre);
        return ResponseEntity.ok(proyectos != null);
    }
    @GetMapping("/{idproyecto}")
    public ResponseEntity<Optional<Proyectos>> getById(@PathVariable Long idproyecto){
        Proyectos proyecto = proyectosService.findById(idproyecto).orElse(null);
        if(proyecto != null ){
            return ResponseEntity.ok(Optional.of(proyecto));
        }else {
            return ResponseEntity.noContent().build();
        }
    }
    @DeleteMapping("/{idproyecto}")
    public  ResponseEntity<Object> deleteProyecto(@PathVariable Long idproyecto){
        return ResponseEntity.ok(proyectosService.delete(idproyecto));
    }
    @GetMapping("/cod-mayor")
    public ResponseEntity<List<Proyectos>> getByCodigoMayor (@RequestParam String codigo){
        return ResponseEntity.ok(proyectosService.findByCodigoMayor(codigo));
    }
    @GetMapping("/reportes/porniveles")
    public ResponseEntity<List<Proyectos_rep_int>> getByNivel(@RequestParam Long nivel){
        return ResponseEntity.ok(proyectosService.findByNivel(nivel));
    }
    @GetMapping("/reportes/porgrupo")
    public ResponseEntity<List<Proyectos_rep_int>> getByGrupo(@RequestParam String codigo){
        return ResponseEntity.ok(proyectosService.findByGrupo(codigo));
    }
    @GetMapping("/codnom")
    public ResponseEntity<List<Proyectos>> getByCodNom(@RequestParam String dato){
        return ResponseEntity.ok(proyectosService.findByCodNom(dato));
    }





    /*REPORTES DE JASPER REPORT*/
    @GetMapping("/reportes/proyectos/findall")
    public ResponseEntity<Resource> listAll()
            throws JRException, IOException, SQLException {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("fileName", "listaProyectos");

        ReportDTO dto = reportI.generateReport(params);
        InputStreamResource streamResource = new InputStreamResource(dto.getStream());
        MediaType mediaType = null;
        mediaType = MediaType.APPLICATION_PDF;

        return ResponseEntity.ok().header("Content-Disposition", "inline; filename=\"" + dto.getFileName() + "\"")
                .contentLength(dto.getLength()).contentType(mediaType).body(streamResource);
    }
    @GetMapping("/reportes/proyectos/bynivel")
    public ResponseEntity<Resource> listByNivel(@RequestParam Long nivel)
            throws JRException, IOException, SQLException {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("nivel", nivel);
        params.put("fileName", "listaProyectosByNivel");

        ReportDTO dto = reportI.generateReport(params);
        InputStreamResource streamResource = new InputStreamResource(dto.getStream());
        MediaType mediaType = null;
        mediaType = MediaType.APPLICATION_PDF;

        return ResponseEntity.ok().header("Content-Disposition", "inline; filename=\"" + dto.getFileName() + "\"")
                .contentLength(dto.getLength()).contentType(mediaType).body(streamResource);
    }



}
