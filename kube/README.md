# Kubernetes Website Operator Example

This folder contains example manifests for a custom Kubernetes Operator that manages Website resources.

## Files
- `website-1.yml`, `website-2.yml`: Example custom resources (CRs) of kind `Website`. Each defines a website with a message and replica count.
- `websites.vodafone.com-v1.yml`: CustomResourceDefinition (CRD) for the Website kind.

## Usage
1. Apply the CRD first:
   kubectl apply -f websites.vodafone.com-v1.yml
2. Apply a Website resource:
   kubectl apply -f website-2.yml -n website
   (or use website-1.yml)

## Learning Points
- Shows how to define and use custom resources in Kubernetes.
- Demonstrates how operators manage application-specific logic via CRs.
- Each manifest is annotated for clarity.

See the operator source code in `src/main/java/com/vodadfone/` for implementation details.

