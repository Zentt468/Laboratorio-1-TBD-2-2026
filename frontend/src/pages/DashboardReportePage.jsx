import React, { useState, useEffect, useMemo } from 'react';
import { reporteService } from '../services/reporteService';

export default function DashboardReportePage({ onVolverHome }) {
    const [reporte, setReporte] = useState([]);
    const [cargando, setCargando] = useState(true);
    const [filtroCiudad, setFiltroCiudad] = useState('');
    const [error, setError] = useState(null);

    const nombresMeses = [
        '', 'Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio',
        'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'
    ];

    const cargarDatos = async () => {
        try {
            setCargando(true);
            setError(null);
            const data = await reporteService.getOcupacion();
            setReporte(data);
        } catch (err) {
            console.error('Error al cargar reporte:', err);
            if (err.response?.status === 403) {
                setError('Acceso denegado: Se requiere rol de Administrador.');
            } else {
                setError('No se pudo obtener el reporte desde la base de datos.');
            }
        } finally {
            setCargando(false);
        }
    };

    useEffect(() => {
        cargarDatos();
    }, []);

    // Filtrado reactivo en el frontend por ciudad
    const datosFiltrados = useMemo(() => {
        if (!filtroCiudad.trim()) return reporte;
        return reporte.filter((item) =>
            item.ciudad?.toLowerCase().includes(filtroCiudad.toLowerCase())
        );
    }, [reporte, filtroCiudad]);

    // Totales acumulados para las tarjetas KPI
    const metricas = useMemo(() => {
        const totalIngresos = datosFiltrados.reduce(
            (acc, curr) => acc + (Number(curr.ingresos_totales) || 0),
            0
        );
        const totalReservas = datosFiltrados.reduce(
            (acc, curr) => acc + (Number(curr.total_reservas) || 0),
            0
        );
        return { totalIngresos, totalReservas };
    }, [datosFiltrados]);

    return (
        <div className="container py-5">
            {/* Barra superior */}
            <div className="d-flex justify-content-between align-items-center mb-4">
                <div>
                    <h2 className="fw-bold mb-1">Ocupación e Ingresos Mensuales</h2>
                    <p className="text-muted small mb-0">
                        Vista materializada
                    </p>
                </div>
                <button className="btn btn-outline-secondary rounded-pill btn-sm px-3" onClick={onVolverHome}>
                    Volver
                </button>
            </div>

            {error && <div className="alert alert-danger py-2">{error}</div>}

            {/* Tarjetas KPI */}
            <div className="row g-3 mb-4">
                <div className="col-12 col-md-6">
                    <div className="card border-0 shadow-sm rounded-4 p-3 bg-light">
                        <span className="text-muted small fw-semibold">Ingresos Totales Consolidados</span>
                        <h3 className="fw-bold text-success mt-1 mb-0">
                            ${metricas.totalIngresos.toLocaleString('es-CL')}
                        </h3>
                    </div>
                </div>
                <div className="col-12 col-md-6">
                    <div className="card border-0 shadow-sm rounded-4 p-3 bg-light">
                        <span className="text-muted small fw-semibold">Reservas Totales</span>
                        <h3 className="fw-bold text-primary mt-1 mb-0">
                            {metricas.totalReservas.toLocaleString('es-CL')}
                        </h3>
                    </div>
                </div>
            </div>

            {/* Filtro por ciudad */}
            <div className="mb-3 col-12 col-md-4">
                <input
                    type="text"
                    className="form-control rounded-pill"
                    placeholder="Filtrar por ciudad..."
                    value={filtroCiudad}
                    onChange={(e) => setFiltroCiudad(e.target.value)}
                />
            </div>

            {/* Tabla de resultados */}
            {cargando ? (
                <div className="text-center py-5">
                    <div className="spinner-border text-primary" role="status"></div>
                    <p className="mt-2 text-muted small">Consultando vista materializada...</p>
                </div>
            ) : datosFiltrados.length === 0 ? (
                <div className="text-center py-5 bg-light rounded-4 border">
                    <p className="text-muted mb-0">No hay registros de ocupación o ingresos.</p>
                </div>
            ) : (
                <div className="table-responsive bg-white rounded-4 shadow-sm border p-3">
                    <table className="table table-hover align-middle mb-0">
                        <thead className="table-light">
                        <tr>
                            <th>Ciudad</th>
                            <th>Tipo de Alojamiento</th>
                            <th>Período</th>
                            <th className="text-center">Total Reservas</th>
                            <th className="text-end">Ingresos Totales</th>
                        </tr>
                        </thead>
                        <tbody>
                        {datosFiltrados.map((fila, index) => (
                            <tr key={`${fila.ciudad}-${fila.tipo_alojamiento}-${fila.anio}-${fila.mes}-${index}`}>
                                <td className="fw-bold">{fila.ciudad}</td>
                                <td>
                                    <span className="badge bg-secondary text-uppercase">{fila.tipo_alojamiento}</span>
                                </td>
                                <td>
                                    {nombresMeses[fila.mes] || fila.mes} {fila.anio}
                                </td>
                                <td className="text-center fw-semibold">{fila.total_reservas}</td>
                                <td className="text-end fw-bold text-dark">
                                    ${Number(fila.ingresos_totales).toLocaleString('es-CL')}
                                </td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>
            )}
        </div>
    );
}