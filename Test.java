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
