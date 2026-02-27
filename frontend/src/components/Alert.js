// frontend/src/components/Alert.js
import React from 'react';

export default function Alert({ children, type = 'warn' }) {
    const className = type === 'error' ? 'alert alertError' : 'alert';
    return <div className={className}>{children}</div>;
}