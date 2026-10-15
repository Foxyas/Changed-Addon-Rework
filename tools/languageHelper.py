import json
import os
from pathlib import Path

def load_json(filepath):
    """Carrega um arquivo JSON de forma segura."""
    if not filepath or not os.path.exists(filepath):
        return {}
    with open(filepath, 'r', encoding='utf-8') as f:
        try:
            return json.load(f)
        except json.JSONDecodeError as e:
            print(f"Erro ao ler o JSON {filepath}: {e}")
            return {}

def save_json(data, filepath):
    """Salva o dicionário em formato JSON ordenado e identado."""
    with open(filepath, 'w', encoding='utf-8') as f:
        json.dump(data, f, ensure_ascii=False, indent=2)
    print(f"\n[Sucesso] Arquivo salvo em: {filepath}")

def compare_and_merge_lang():
    print("=== Ferramenta de Comparação e Mesclagem de Lang para Minecraft ===")
    
    # Entradas principais
    ref_path = input("Caminho do arquivo de referência/fonte (ex: en_us.json): ").strip()
    target_path = input("Caminho do seu arquivo de tradução atual (ex: pt_br.json): ").strip()
    
    ref_data = load_json(ref_path)
    target_data = load_json(target_path)
    
    if not ref_data:
        print("Erro: O arquivo de referência é obrigatório ou está vazio.")
        return

    ref_keys = set(ref_data.keys())
    target_keys = set(target_data.keys())

    missing_keys = ref_keys - target_keys
    extra_keys = target_keys - ref_keys
    matching_keys = ref_keys & target_keys

    print(f"\n--- Relatório de Comparação ---")
    print(f"Total na Referência: {len(ref_keys)}")
    print(f"Total na Tradição Atual: {len(target_keys)}")
    print(f"Chaves Faltantes (Novas no mod): {len(missing_keys)}")
    print(f"Chaves Extras (Obsoletas/Removidas): {len(extra_keys)}")

    # Resultado base inicial
    result_data = {}

    # Opção 2: Política de conteúdo de chaves existentes
    print("\n[Opção 2] Como deseja tratar as chaves já traduzidas?")
    print("1. Manter a tradução atual (Padrão)")
    print("2. Substituir pelo valor da referência (útil se mudou o texto em inglês)")
    choice_policy = input("Escolha (1 ou 2): ").strip()

    for key in ref_keys:
        if key in target_data:
            if choice_policy == "2":
                result_data[key] = ref_data[key]  # Substitui
            else:
                result_data[key] = target_data[key] # Mantém
        else:
            # Chave nova, preenche inicialmente com o valor de referência ou vazio
            result_data[key] = f"TODO: {ref_data[key]}"

    # Opção 1: Requisitar outro JSON file para mixar traduções faltando
    print("\n[Opção 1] Deseja carregar um arquivo JSON secundário de patch/traduções parciais para mesclar?")
    patch_path = input("Caminho do arquivo patch JSON (ou pressione Enter para pular): ").strip()
    if patch_path:
        patch_data = load_json(patch_path)
        mixed_count = 0
        for k, v in patch_data.items():
            if k in result_data:
                result_data[k] = v
                mixed_count += 1
        print(f"-> {mixed_count} chaves atualizadas com o patch secundário.")

    # Opção 3: Adicionar manualmente chaves com tradução faltando ao resultado
    print("\n[Opção 3] Deseja adicionar manualmente traduções para alguma chave faltante agora?")
    while True:
        add_more = input("Adicionar chave manual? (s/n): ").strip().lower()
        if add_more != 's':
            break
        manual_key = input("Digite a chave exata (ex: item.changed.foo): ").strip()
        if manual_key in ref_data:
            manual_val = input(f"Digite a tradução para [{manual_key}]: ").strip()
            result_data[manual_key] = manual_val
            print("-> Adicionado com sucesso!")
        else:
            print("-> Aviso: Essa chave não existe no arquivo de referência.")

    # Salvar saída
    output_path = input("\nCaminho para salvar o novo arquivo JSON gerado (ex: pt_br_updated.json): ").strip()
    if output_path:
        save_json(result_data, output_path)

if __name__ == "__main__":
    compare_and_merge_lang()