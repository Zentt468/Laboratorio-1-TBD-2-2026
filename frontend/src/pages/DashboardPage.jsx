import React, { useState, useEffect } from 'react';
import { reporteService } from '../services/reporteService';

export default function DashboardPage({ onVolverHome }) {
    const [reporte, setReporte] = useState([]);
    const [cargando, setCargando] = useState(true);
    const [error, setError] = useState(null);

    const cargar = async (refrescar = false) => {
        try {
            setCargando(true);
            const data = refrescar
                ? await reporteService.refrescar()
                : await reporteService.getOcupacion();
            setReporte(data);
            setError(null);
        } catch (err) {
            console.error('Error al cargar el reporte:', err);
            setError('No tienes permisos para ver esta sección o hubo un fallo en el servidor.');
        } finally {
            setCargando(false);
        }
    };

    useEffect(() => {
        cargar();
    }, []);

    const formatoPesos = (valor) => `$${Number(valor || 0).toLocaleString('es-CL')}`;
    const nombreMes = (mes) =>
        new Date(2000, mes - 1, 1).toLocaleString('es-CL', { month: 'long' });

    // Totales generales, calculados a partir de las filas de la vista
    const totalReservas = reporte.reduce((suma, fila) => suma + fila.total_reservas, 0);
    const totalIngresos = reporte.reduce((suma, fila) => suma + fila.ingresos_totales, 0);
    const ciudades = new Set(reporte.map((fila) => fila.ciudad)).size;

    return (
        <div className="container py-5">
            <div className="d-flex justify-content-between align-items-center mb-4">
                <div>
                    <h2 className="fw-bold mb-1">Dashboard de administración</h2>
                    <p className="text-muted small mb-0">
                        Ocupación e ingresos mensuales por ciudad y tipo de alojamiento (vista materializada)
                    </p>
                </div>
                <div className="d-flex gap-2">
                    <button
                        className="btn btn-outline-primary rounded-pill btn-sm px-3"
                        onClick={() => cargar(true)}
                        disabled={cargando}
                    >
                        <i className="bi bi-arrow-clockwise me-1"></i> Refrescar vista
                    </button>
                    <button className="btn btn-outline-secondary rounded-pill btn-sm px-3" onClick={onVolverHome}>
                        Volver al Inicio
                    </button>
                </div>
            </div>

            {error && <div className="alert alert-danger py-2">{error}</div>}

            {cargando ? (
                <div className="text-center py-5">
                    <div className="spinner-border text-primary" role="status"></div>
                    <p className="mt-2 text-muted small">Cargando reporte...</p>
                </div>
            ) : reporte.length === 0 ? (
                <div className="text-center py-5 bg-light rounded-4 border">
                    <p className="text-muted mb-0">No hay datos en el reporte.</p>
                </div>
            ) : (
                <>
                    {/* Totales */}
                    <div className="row g-3 mb-4">
                        <div className="col-12 col-md-4">
                            <div className="p-3 bg-white rounded-4 shadow-sm border">
                                <div className="text-muted small">Reservas</div>
                                <div className="fs-3 fw-bold">{totalReservas}</div>
                            </div>
                        </div>
                        <div className="col-12 col-md-4">
                            <div className="p-3 bg-white rounded-4 shadow-sm border">
                                <div className="text-muted small">Ingresos</div>
                                <div className="fs-3 fw-bold">{formatoPesos(totalIngresos)}</div>
                            </div>
                        </div>
                        <div className="col-12 col-md-4">
                            <div className="p-3 bg-white rounded-4 shadow-sm border">
                                <div className="text-muted small">Ciudades con reservas</div>
                                <div className="fs-3 fw-bold">{ciudades}</div>
                            </div>
                        </div>
                    </div>

                    {/* Detalle por mes, ciudad y tipo */}
                    <div className="table-responsive bg-white rounded-4 shadow-sm border p-3">
                        <table className="table table-hover align-middle mb-0">
                            <thead className="table-light">
                            <tr>
                                <th>Período</th>
                                <th>Ciudad</th>
                                <th>Tipo de alojamiento</th>
                                <th className="text-end">Reservas</th>
                                <th className="text-end">Ingresos (CLP)</th>
                            </tr>
                            </thead>
                            <tbody>
                            {reporte.map((fila) => (
                                <tr key={`${fila.anio}-${fila.mes}-${fila.ciudad}-${fila.tipo_alojamiento}`}>
                                    <td className="text-capitalize">{nombreMes(fila.mes)} {fila.anio}</td>
                                    <td>{fila.ciudad}</td>
                                    <td>{fila.tipo_alojamiento}</td>
                                    <td className="text-end">{fila.total_reservas}</td>
                                    <td className="text-end fw-semibold">{formatoPesos(fila.ingresos_totales)}</td>
                                </tr>
                            ))}
                            </tbody>
                        </table>
                    </div>
                </>
            )}
        </div>
    );
}