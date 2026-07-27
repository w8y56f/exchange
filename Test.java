di() {
  docker images "$@" --format "table {{.Repository}}\t{{.Tag}}\t{{.ID | printf \"%.12s\"}}\t{{.Size}}\t{{.CreatedSince}}" \
    | awk '{printf "%-4s %s\n", (NR==1 ? "#" : NR-1), $0}'
}
dps() {
  docker ps "$@" --format "table {{.ID | printf \"%.12s\"}}\t{{.Image}}\t{{.Status}}\t{{.Names | printf \"%.20s\"}}" \
    | awk '{printf "%-4s %s\n", (NR==1 ? "#" : NR-1), $0}'
}
alias dpsa='dps -a'

# 快捷切换 Kubernetes 命名空间
kcd() {
    if [ -z "$1" ]; then
        current_ns=$(kubectl config view --minify --output 'jsonpath={..namespace}')
        #echo "Current namespace1111: $current_ns"
        display_ns=${current_ns:-default}
        echo "Usage: kcd <namespace_name>"
        echo "Current namespace: $display_ns"
        return 1
    fi

    # Check if the provided namespace exists
    if kubectl get namespace "$1" &> /dev/null; then
        kubectl config set-context --current --namespace="$1" &> /dev/null
        echo "Switched to namespace '$1'"
    else
        echo "Error: Namespace '$1' does not exist."
        return 1
    fi
}
img() {
  local pod="$1"
  # 获取第二个参数；若未指定则尝试获取当前 kubeconfig 默认命名空间
  local ns="${2:-$(kubectl config view --minify -o jsonpath='{..namespace}')}"

  if [[ -z "$pod" ]]; then
    echo "Usage: img <pod-name> [namespace]" >&2
    return 1
  fi

  if [[ -z "$ns" ]]; then
    ns="default"
  fi

  # 使用 $'...\n...' 语法确保输出真正的换行符
  kubectl -n "$ns" get pod "$pod" -o jsonpath=$'{range .spec.containers[*]}{.name}\t{.image}\n{end}'
  local rc=$?

  echo "namespace: $ns" >&2
  return $rc
}
