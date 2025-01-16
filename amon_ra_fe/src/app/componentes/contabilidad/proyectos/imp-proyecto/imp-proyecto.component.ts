import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { jsPDF } from 'jspdf';

import {
  FormBuilder,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
} from '@angular/forms';
import { EstructuraService } from '../../../../servicioss/contabilidad/estructura.service';
import { ProyectosService } from '../../../../servicioss/contabilidad/proyectos.service';
import { PdfService } from '../../../../servicioss/reportes/pdf.service';

@Component({
  selector: 'app-imp-proyecto',
  imports: [FormsModule, CommonModule, ReactiveFormsModule],
  templateUrl: './imp-proyecto.component.html',
  styleUrl: './imp-proyecto.component.css',
})
export class ImpProyectoComponent implements OnInit {
  f_reporte!: FormGroup;
  _estructura?: any;
  listaReportes: any[] = [
    {
      value: 0,
      nombre: 'Todos los proyectos',
    },
    {
      value: 1,
      nombre: 'Proyectos por nivel',
    },
    {
      value: 2,
      nombre: 'Proyectos por grupo',
    },
  ];
  datosImprimir: any = [];
  constructor(
    private fb: FormBuilder,
    private estructuraService: EstructuraService,
    private proyectoService: ProyectosService,
    private pdfService: PdfService
  ) {}
  ngOnInit(): void {
    this.f_reporte = this.fb.group({
      reporte: 0,
      nivel: 0,
      grupo: 0,
    });
    this.getEstructura();
  }
  getEstructura() {
    this.estructuraService.estructuraGetAll().subscribe({
      next: (datos: any) => {
        this._estructura = datos;
      },
      error: (e: any) => console.error(e),
    });
  }
  getAllProyectos(doc: any, header: any) {
    this.proyectoService.proyectosGetAll().subscribe({
      next: async (datos: any) => {
        console.log(datos);
        datos.forEach(async (item: any) => {
          await this.datosImprimir.push([
            item.codigo,
            item.nombre,
            item.movimiento,
            item.idestructura_estructura.nombre,
            item.idestructura_estructura.nivel,
          ]);
        });
        this.pdfService.pdfOneTable(doc, header, this.datosImprimir);
      },
      error: (e: any) => console.error(e),
    });
  }
  onSubmit() {
    let opt: number = this.f_reporte.value.reporte;
    let nombre: string = this.f_reporte.value.nombre;
    const doc = new jsPDF();
    switch (opt) {
      case 0:
        let header: any[] = [
          ['Lista proyectos'],
          ['Código', 'Nombre', 'Movimiento', 'Estructura', 'Nivel'],
        ];
        try {
          this.getAllProyectos(doc, header);
        } catch (e: any) {
          console.error(e);
        }
        break;
      case 1:
        console.log('uno');
        break;
      case 2:
        console.log('dos');
        break;
    }
  }
}
