#!/usr/bin/env python3
"""
Script para gerar relatório HTML dos testes a partir dos XMLs do Surefire
"""

import xml.etree.ElementTree as ET
import os
import sys
from pathlib import Path
from datetime import datetime

def parse_test_file(file_path):
    """Parse XML de teste e extrai informações"""
    tree = ET.parse(file_path)
    root = tree.getroot()
    
    data = {
        'name': root.get('name', 'Unknown'),
        'tests': int(root.get('tests', 0)),
        'failures': int(root.get('failures', 0)),
        'errors': int(root.get('errors', 0)),
        'skipped': int(root.get('skipped', 0)),
        'time': float(root.get('time', 0)),
        'test_cases': []
    }
    
    for testcase in root.findall('testcase'):
        case = {
            'name': testcase.get('name', 'Unknown'),
            'classname': testcase.get('classname', 'Unknown'),
            'time': float(testcase.get('time', 0)),
            'status': 'PASSED'
        }
        
        # Verificar status
        if testcase.find('failure') is not None:
            case['status'] = 'FAILED'
            case['message'] = testcase.find('failure').get('message', '')
        elif testcase.find('error') is not None:
            case['status'] = 'ERROR'
            case['message'] = testcase.find('error').get('message', '')
        elif testcase.find('skipped') is not None:
            case['status'] = 'SKIPPED'
        
        data['test_cases'].append(case)
    
    return data

def generate_html_report(test_data_list, output_file):
    """Gera relatório HTML"""
    
    total_tests = sum(d['tests'] for d in test_data_list)
    total_failures = sum(d['failures'] for d in test_data_list)
    total_errors = sum(d['errors'] for d in test_data_list)
    total_skipped = sum(d['skipped'] for d in test_data_list)
    total_time = sum(d['time'] for d in test_data_list)
    success_rate = ((total_tests - total_failures - total_errors) / total_tests * 100) if total_tests > 0 else 0
    
    html = f"""
    <!DOCTYPE html>
    <html lang="pt-BR">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Relatório de Testes - QA API Automation</title>
        <style>
            * {{ margin: 0; padding: 0; box-sizing: border-box; }}
            body {{ 
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                min-height: 100vh;
                padding: 20px;
            }}
            .container {{ 
                max-width: 1200px; 
                margin: 0 auto;
                background: white;
                border-radius: 10px;
                box-shadow: 0 10px 40px rgba(0,0,0,0.3);
                overflow: hidden;
            }}
            .header {{
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                color: white;
                padding: 40px 20px;
                text-align: center;
            }}
            .header h1 {{
                font-size: 2.5em;
                margin-bottom: 10px;
            }}
            .header p {{
                opacity: 0.9;
                font-size: 0.95em;
            }}
            .metrics {{
                display: grid;
                grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
                gap: 20px;
                padding: 30px;
                background: #f8f9fa;
            }}
            .metric {{
                background: white;
                padding: 20px;
                border-radius: 8px;
                box-shadow: 0 2px 10px rgba(0,0,0,0.1);
                text-align: center;
                border-left: 4px solid #667eea;
            }}
            .metric h3 {{
                color: #666;
                font-size: 0.9em;
                margin-bottom: 10px;
                text-transform: uppercase;
            }}
            .metric-value {{
                font-size: 2em;
                font-weight: bold;
                color: #667eea;
            }}
            .metric.success {{ border-left-color: #28a745; }}
            .metric.success .metric-value {{ color: #28a745; }}
            .metric.failure {{ border-left-color: #dc3545; }}
            .metric.failure .metric-value {{ color: #dc3545; }}
            .metric.error {{ border-left-color: #ffc107; }}
            .metric.error .metric-value {{ color: #ffc107; }}
            .metric.skipped {{ border-left-color: #6c757d; }}
            .metric.skipped .metric-value {{ color: #6c757d; }}
            
            .progress-bar {{
                width: 100%;
                height: 30px;
                background: #e9ecef;
                border-radius: 15px;
                overflow: hidden;
                margin-top: 10px;
            }}
            .progress-fill {{
                height: 100%;
                background: linear-gradient(90deg, #28a745 0%, #20c997 100%);
                display: flex;
                align-items: center;
                justify-content: center;
                color: white;
                font-weight: bold;
                font-size: 0.9em;
            }}
            
            .content {{
                padding: 30px;
            }}
            .content h2 {{
                color: #333;
                margin-bottom: 20px;
                border-bottom: 2px solid #667eea;
                padding-bottom: 10px;
            }}
            .test-group {{
                margin-bottom: 30px;
                background: #f8f9fa;
                border-radius: 8px;
                overflow: hidden;
            }}
            .test-group-header {{
                background: #667eea;
                color: white;
                padding: 15px 20px;
                font-weight: bold;
                display: flex;
                justify-content: space-between;
                align-items: center;
            }}
            .test-group-stats {{
                display: flex;
                gap: 20px;
                font-size: 0.9em;
            }}
            .test-group-stats span {{
                display: flex;
                align-items: center;
                gap: 5px;
            }}
            .badge {{
                display: inline-block;
                padding: 4px 12px;
                border-radius: 20px;
                font-size: 0.85em;
                font-weight: bold;
            }}
            .badge.passed {{ background: #d4edda; color: #155724; }}
            .badge.failed {{ background: #f8d7da; color: #721c24; }}
            .badge.error {{ background: #fff3cd; color: #856404; }}
            .badge.skipped {{ background: #e2e3e5; color: #383d41; }}
            
            .test-list {{
                padding: 0;
            }}
            .test-item {{
                padding: 15px 20px;
                border-bottom: 1px solid #dee2e6;
                display: flex;
                justify-content: space-between;
                align-items: center;
            }}
            .test-item:last-child {{
                border-bottom: none;
            }}
            .test-name {{
                flex: 1;
                color: #333;
            }}
            .test-time {{
                color: #6c757d;
                font-size: 0.9em;
                margin-right: 20px;
            }}
            
            .footer {{
                background: #f8f9fa;
                padding: 20px;
                text-align: center;
                color: #6c757d;
                font-size: 0.9em;
                border-top: 1px solid #dee2e6;
            }}
        </style>
    </head>
    <body>
        <div class="container">
            <div class="header">
                <h1>📊 Relatório de Testes</h1>
                <p>QA API Automation - Dog Breeds API</p>
            </div>
            
            <div class="metrics">
                <div class="metric success">
                    <h3>Total de Testes</h3>
                    <div class="metric-value">{total_tests}</div>
                </div>
                <div class="metric success">
                    <h3>Testes Passados</h3>
                    <div class="metric-value">{total_tests - total_failures - total_errors}</div>
                </div>
                <div class="metric failure">
                    <h3>Falhas</h3>
                    <div class="metric-value">{total_failures}</div>
                </div>
                <div class="metric error">
                    <h3>Erros</h3>
                    <div class="metric-value">{total_errors}</div>
                </div>
                <div class="metric skipped">
                    <h3>Ignorados</h3>
                    <div class="metric-value">{total_skipped}</div>
                </div>
                <div class="metric success">
                    <h3>Taxa de Sucesso</h3>
                    <div class="metric-value">{success_rate:.1f}%</div>
                    <div class="progress-bar">
                        <div class="progress-fill" style="width: {success_rate}%">{success_rate:.1f}%</div>
                    </div>
                </div>
            </div>
            
            <div class="content">
                <h2>⏱️ Tempo de Execução: {total_time:.2f}s</h2>
                
                <h2>📝 Detalhes dos Testes</h2>
    """
    
    for test_data in test_data_list:
        class_name = test_data['name'].split('.')[-1]
        passed = test_data['tests'] - test_data['failures'] - test_data['errors']
        
        html += f"""
                <div class="test-group">
                    <div class="test-group-header">
                        <span>{class_name}</span>
                        <div class="test-group-stats">
                            <span><span class="badge passed">✓ {passed}</span></span>
                            <span><span class="badge failed">✗ {test_data['failures']}</span></span>
                            <span><span class="badge error">⚠ {test_data['errors']}</span></span>
                            <span style="margin-left: 10px;">{test_data['time']:.2f}s</span>
                        </div>
                    </div>
                    <div class="test-list">
        """
        
        for case in test_data['test_cases']:
            status_badge = f'<span class="badge {case["status"].lower()}">{"✓" if case["status"] == "PASSED" else "✗" if case["status"] == "FAILED" else "⚠"} {case["status"]}</span>'
            html += f"""
                        <div class="test-item">
                            <div class="test-name">{case['name']}</div>
                            <div class="test-time">{case['time']:.3f}s</div>
                            <div>{status_badge}</div>
                        </div>
            """
        
        html += """
                    </div>
                </div>
        """
    
    html += f"""
            </div>
            
            <div class="footer">
                <p>📅 Gerado em: {datetime.now().strftime('%d/%m/%Y às %H:%M:%S')}</p>
                <p>🔗 Dog API: https://dog.ceo/api</p>
            </div>
        </div>
    </body>
    </html>
    """
    
    with open(output_file, 'w', encoding='utf-8') as f:
        f.write(html)

def main():
    """Função principal"""
    reports_dir = Path('target/surefire-reports')
    
    if not reports_dir.exists():
        print("❌ Diretório de relatórios não encontrado!")
        return False
    
    xml_files = list(reports_dir.glob('*.xml'))
    
    if not xml_files:
        print("❌ Nenhum arquivo XML de teste encontrado!")
        return False
    
    print(f"📋 Processando {len(xml_files)} arquivo(s) de teste...")
    
    test_data_list = []
    for xml_file in sorted(xml_files):
        try:
            data = parse_test_file(xml_file)
            test_data_list.append(data)
            print(f"  ✓ {data['name']}: {data['tests']} testes")
        except Exception as e:
            print(f"  ❌ Erro ao processar {xml_file}: {e}")
    
    output_file = reports_dir / 'index.html'
    generate_html_report(test_data_list, output_file)
    
    print(f"\n✅ Relatório gerado: {output_file}")
    return True

if __name__ == '__main__':
    success = main()
    sys.exit(0 if success else 1)
