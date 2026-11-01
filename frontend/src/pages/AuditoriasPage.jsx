import React, { useState, useEffect } from 'react';
import { auditoriaService } from '../services/auditoriaService';

export default function AuditoriasPage({ onVolverHome }) {
    const [auditorias, setAuditorias] = useState([]);
    const [cargando, setCargando] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const cargarDatos = async () => {
            try {
                setCargando(true);
                const data = await auditoriaService.getAll();
                setAuditorias(data);
            } catch (err) {
                console.error('Error al cargar auditorías:', err);
                setError('No tienes permisos para ver esta sección o hubo un fallo en el servidor.');
            } finally {
                setCargando(false);
            }
        };

        cargarDatos();
    }, []);

    const getOperacionBadge = (operacion) => {
        switch (operacion?.toUpperCase()) {
            case 'INSERT':
                return 'bg-success';
            case 'UPDATE':
                return 'bg-warning text-dark';
            case 'DELETE':
                return 'bg-danger';
            default:
                return 'bg-secondary';
        }
    };

    const formatearFecha = (fechaStr) => {
        if (!fechaStr) return '-';
        return new Date(fechaStr).toLocaleString('es-CL');
    };

    return (
        <div className="container py-5">
            <div className="d-flex justify-content-between align-items-center mb-4">
                <div>
                    <h2 className="fw-bold mb-1">Registro de Auditorías</h2>
                    <p className="text-muted small mb-0">Registros generados por triggers en la base de datos</p>
                </div>
                <button className="btn btn-outline-secondary rounded-pill btn-sm px-3" onClick={onVolverHome}>
                    Volver al Inicio
                </button>
            </div>

            {error && <div className="alert alert-danger py-2">{error}</div>}

            {cargando ? (
                <div className="text-center py-5">
                    <div className="spinner-border text-primary" role="status"></div>
                    <p className="mt-2 text-muted small">Cargando auditorías...</p>
                </div>
            ) : auditorias.length === 0 ? (
                <div className="text-center py-5 bg-light rounded-4 border">
                    <p className="text-muted mb-0">No se encontraron eventos de auditoría registrados.</p>
                </div>
            ) : (
                <div className="table-responsive bg-white rounded-4 shadow-sm border p-3">
                    <table className="table table-hover align-middle mb-0">
                        <thead className="table-light">
                        <tr>
                            <th>ID</th>
                            <th>Operación</th>
                            <th>Tabla</th>
                            <th>Usuario</th>
                            <th>Fecha y Hora</th>
                            <th>Detalle / Registro</th>
                        </tr>
                        </thead>
                        <tbody>
                        {auditorias.map((item) => (
                            <tr key={item.idAuditoria}>
                                <td className="fw-semibold">#{item.idAuditoria}</td>
                                <td>
                    <span className={`badge rounded-pill ${getOperacionBadge(item.operacion)}`}>
                      {item.operacion}
                    </span>
                                </td>
                                <td className="fw-bold text-secondary">{item.tabla}</td>
                                <td>{item.usuario || 'Sistema/BD'}</td>
                                <td className="small text-muted">{formatearFecha(item.fecha)}</td>
                                <td>
                                    <code className="small bg-light p-1 rounded text-dark">
                                        {typeof item.detalle === 'object' ? JSON.stringify(item.detalle) : item.detalle || '-'}
                                    </code>
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